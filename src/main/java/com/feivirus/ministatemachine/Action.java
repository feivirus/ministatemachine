package com.feivirus.ministatemachine;

public interface Action<T extends StateMachine<T, S, E, C>, S, E, C> {
	void execute(S from, S to, E event, C context, T stateMachine);
}
