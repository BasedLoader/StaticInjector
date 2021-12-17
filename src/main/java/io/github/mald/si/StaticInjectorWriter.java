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
		this.writer.write(StaticInjector.STATIC_INJECTOR);
		this.writer.write(' ');
		this.writer.write(version);
		this.writer.write('\n');
	}

	@Override
	public void visitInterfaceExtension(Side side, String targetClass, String interfaceClass) throws IOException {
		this.writer.write(side.name().toLowerCase(Locale.ROOT));
		this.writer.write(" interface ");
		this.writer.write(targetClass);
		this.writer.write(' ');
		this.writer.write(interfaceClass);
		this.writer.write('\n');
	}

	@Override
	public void visitComment(String rawContent, String realContent) throws IOException {
		this.writer.write(rawContent);
		this.writer.write('\n');
	}

	@Override
	public void visitEmptyLine() throws IOException {
		this.writer.write('\n');
	}
}
