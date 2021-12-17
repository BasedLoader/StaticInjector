package io.github.mald.si;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public interface StaticInjectorVisitor {
	enum Side {
		CLIENT,
		SERVER,
		COMMON;
		public static final List<Side> SIDES = Collections.unmodifiableList(Arrays.asList(values()));
	}

	void visitHeader(String version) throws IOException;

	void visitInterfaceExtension(Side side, String targetClass, String interfaceClass) throws IOException;

	default void visitComment(String rawContent, String realContent) throws IOException {}

	default void visitEmptyLine() throws IOException {}
}
