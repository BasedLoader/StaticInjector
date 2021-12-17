package io.github.mald.si;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class StaticInjector {
	public static final String LINE_DESC = "Expected \"(common/client/server) interface <target class> <interface class name>\" found ";
	public static final String HEADER_DESC = "Expected \"staticInjector <version>\" found ";
	public static final String STATIC_INJECTOR = "staticInjector";
	public static final String VERSION = "v1";
	public static void read(BufferedReader source, StaticInjectorVisitor visitor) throws IOException {
		String header;
		while((header = source.readLine()) != null) {
			if(!visitFluff(visitor, header)) {
				break;
			}
		}
		if(header == null) {
			throw new EOFException(HEADER_DESC + "EOF");
		}

		String[] split = header.split("\\s+", 3);
		if(split.length != 2) {
			throw new IllegalArgumentException(HEADER_DESC + "\"" + header + "\"");
		}

		if(!(STATIC_INJECTOR.equals(split[0]) && VERSION.equals(split[1]))) {
			throw new IllegalArgumentException(HEADER_DESC + "\"" + header + "\"");
		}

		visitor.visitHeader(split[1]);

		String line;
		while((line = source.readLine()) != null) {
			if(visitFluff(visitor, line)) {
				continue;
			}

			String[] ln = line.split("\\s+");
			if(ln.length < 2) {
				throw new IllegalArgumentException(LINE_DESC + line);
			}

			StaticInjectorVisitor.Side side;
			int next = 1;
			String first = ln[0];
			switch(first) {
				case "client":
					side = StaticInjectorVisitor.Side.CLIENT;
					break;
				case "server":
					side = StaticInjectorVisitor.Side.SERVER;
					break;
				case "common":
					side = StaticInjectorVisitor.Side.COMMON;
					break;
				default:
					side = StaticInjectorVisitor.Side.COMMON;
					next = 0;
					break;
			}

			if(ln[next].equals("interface") && ln.length == next + 3) {
				visitor.visitInterfaceExtension(side, ln[next+1], ln[next+2]);
			} else {
				throw new IllegalArgumentException(LINE_DESC + line);
			}
		}
	}

	private static boolean visitFluff(StaticInjectorVisitor visitor, String line) throws IOException {
		if(line.isEmpty()) {
			visitor.visitEmptyLine();
			return true;
		} else if(line.startsWith("//")) {
			visitor.visitComment(line, line.substring(2));
			return true;
		} else if(line.startsWith("#")) {
			visitor.visitComment(line, line.substring(1));
			return true;
		}
		return false;
	}

	public static void main(String[] args) {
		try(BufferedReader reader = Files.newBufferedReader(Paths.get("test.si"))) {
			StaticInjector.read(reader, new StaticInjectorVisitor() {
				@Override
				public void visitHeader(String version) {
					System.out.println(version);
				}

				@Override
				public void visitInterfaceExtension(Side side, String targetClass, String interfaceClass) {
					System.out.println(side + " " + targetClass + " " + interfaceClass);
				}
			});
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
