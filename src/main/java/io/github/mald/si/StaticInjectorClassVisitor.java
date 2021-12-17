package io.github.mald.si;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Set;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

public class StaticInjectorClassVisitor extends ClassVisitor {
	private static final Field API;
	static {
		try {
			API = ClassVisitor.class.getDeclaredField("api");
			API.setAccessible(true);
		} catch(NoSuchFieldException e) {
			throw new RuntimeException(e);
		}
	}

	public final StaticInjectorTree tree;
	public final StaticInjectorVisitor.Side side;

	public StaticInjectorClassVisitor(ClassVisitor classVisitor, StaticInjectorTree tree, StaticInjectorVisitor.Side side) {
		super(getApi(classVisitor), classVisitor);
		this.tree = tree;
		this.side = side;
	}

	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		Set<String> toAdd = this.tree.getFor(side, name);
		if(toAdd.isEmpty()) {
			super.visit(version, access, name, signature, superName, interfaces);
		} else {
			String[] ifaces = Arrays.copyOf(interfaces, interfaces.length + toAdd.size());
			int i = interfaces.length;
			for(String attach : toAdd) {
				ifaces[i++] = attach;
			}
			String sign;
			if(signature == null) {
				sign = null;
			} else {
				StringBuilder builder = new StringBuilder(signature);
				for(String iface : toAdd) {
					builder.append('L').append(iface).append(';');
				}
				sign = builder.toString();
			}

			super.visit(version, access, name, sign, superName, ifaces);
		}
	}

	public static int getApi(ClassVisitor visitor) {
		try {
			return API.getInt(visitor);
		} catch(IllegalAccessException e) {
			Warn.init();
			return Opcodes.ASM9;
		}
	}

	private enum Warn {;
		static {
			System.out.println("Unable to access ClassVisitor#api defaulting to ASM9 api");
		}
		static void init() {}
	}
}
