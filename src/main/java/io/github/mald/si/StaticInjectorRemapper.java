package io.github.mald.si;

import java.io.IOException;

import org.objectweb.asm.commons.Remapper;

public class StaticInjectorRemapper extends ForwardingStaticInjectorVisitor {
	public final Remapper remapper;

	public StaticInjectorRemapper(StaticInjectorVisitor visitor, Remapper remapper) {
		super(visitor);
		this.remapper = remapper;
	}

	@Override
	public void visitInterfaceExtension(Side side, String targetClass, String interfaceClass) throws IOException {
		super.visitInterfaceExtension(
				side,
				this.remapper.map(targetClass),
				this.remapper.map(interfaceClass));
	}
}
