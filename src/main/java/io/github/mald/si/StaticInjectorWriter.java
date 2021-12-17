package io.github.mald.si;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Locale;

public class StaticInjectorWriter implements StaticInjectorVisitor {
	public final BufferedWriter writer;

	public StaticInjectorWriter(BufferedWriter writer) {
		this.writer = writer;
	}

	@Override
	public void visitHeader(String version) throws IOException {
		writer.write(StaticInjector.STATIC_INJECTOR);
		writer.write(' ');
		writer.write(version);
		writer.write('\n');
	}

	@Override
	public void visitInterfaceExtension(Side side, String targetClass, String interfaceClass) throws IOException {
		writer.write(side.name().toLowerCase(Locale.ROOT));
		writer.write(" interface ");
		writer.write(targetClass);
		writer.write(' ');
		writer.write(interfaceClass);
		writer.write('\n');
	}

	@Override
	public void visitComment(String rawContent, String realContent) throws IOException {
		writer.write(rawContent);
		writer.write('\n');
	}

	@Override
	public void visitEmptyLine() throws IOException {
		writer.write('\n');
	}
}
