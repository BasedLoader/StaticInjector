package io.github.mald.si;

import java.io.IOException;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class StaticInjectorTree implements StaticInjectorVisitor {
	private final Map<Side, Map<String, Set<String>>> tree = new EnumMap<>(Side.class);

	public StaticInjectorTree() {
		for(Side value : Side.SIDES) {
			this.tree.put(value, new HashMap<>());
		}
	}

	@Override
	public void visitHeader(String version) throws IOException {
	}

	@Override
	public void visitInterfaceExtension(Side side, String targetClass, String interfaceClass) throws IOException {
		this.tree.get(side).computeIfAbsent(targetClass, t -> new HashSet<>()).add(interfaceClass);
		if(side != Side.COMMON) {
			this.tree.get(Side.COMMON).computeIfAbsent(targetClass, t -> new HashSet<>()).add(interfaceClass);
		}
	}

	public Set<String> getTargets(Side side) {
		return this.tree.get(side).keySet();
	}

	public Set<String> getFor(Side side, String targetClass) {
		return this.tree.get(side).getOrDefault(targetClass, Collections.emptySet());
	}
}
