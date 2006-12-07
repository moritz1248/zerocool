package com.zerocool.scene;

import com.jme.scene.Node;

@SuppressWarnings("serial")
public abstract class UpdatableNode extends Node {
	abstract void Update(int elapsed);
}
