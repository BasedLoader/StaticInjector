package io.github.mald.si;

import java.io.IOException;

public abstract class ForwardingStaticInjectorVisitor implements StaticInjectorVisitor {
	protected final StaticInjectorVisitor visitor;

	protected ForwardingStaticInjectorVisitor(StaticInjectorVisitor visitor) {
		this.visitor = visitor;
	}

	protected StaticInjectorVisitor getDelegate() {
		return this.visitor;
	}

	@Override
	public void visitHeader(String version) throws IOException {
		this.getDelegate().visitHeader(version);
	}

	@Override
	public void visitInterfaceExtension(Side side, String targetClass, String interfaceClass) throws IOException {
		this.getDelegate().visitInterfaceExtension(side, targetClass, interfaceClass);
	}

	@Override
	public void visitComment(String rawContent, String realContent) throws IOException {
		this.getDelegate().visitComment(rawContent, realContent);
	}

	@Override
	public void visitEmptyLine() throws IOException {
		this.getDelegate().visitEmptyLine();
	}
}
