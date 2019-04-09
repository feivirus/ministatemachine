package com.feivirus.ministatemachine;

public interface ActionExecutionService<T extends StateMachine<T, S, E, C>, S, E, C> {
	void begin(String bucketName);
	
	void execute();
	
	void defer(Action<T, S, E, C> action, S from, S to, E event, C context, T stateMachine);
}
