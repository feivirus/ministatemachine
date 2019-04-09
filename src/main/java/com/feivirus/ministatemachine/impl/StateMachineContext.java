package com.feivirus.ministatemachine.impl;

import java.util.Stack;

public class StateMachineContext {
	private Object currentInstance;
	
	private static ThreadLocal<Stack<StateMachineContext>> contextHolder = new 
			ThreadLocal<Stack<StateMachineContext>>() {
		protected Stack<StateMachineContext> initialValue() {
			return new Stack<StateMachineContext>();
		}
	};
	
	public StateMachineContext(Object instance) {
		currentInstance = instance;
	}
	
	public static void set(Object instance) {
		if (instance == null) {
			contextHolder.get().pop();
		} else {
			contextHolder.get().push(new StateMachineContext(instance));
		}
	}
	
	public static<T> T currentInstance() {
		return contextHolder.get().size() > 0 ? (T)contextHolder.get().peek().currentInstance : null;
	}

}
