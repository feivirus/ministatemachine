package com.feivirus.ministatemachine;

public interface Transition<T extends StateMachine<T, S, E, C>, S, E, C> {
	
	void setSourceState(State<T, S, E, C> state);
	
	void setTargetState(State<T, S, E, C> state);
	
	void setEvent(E event);
	
	void addAction(Action<T, S, E, C> newAction);
	
	void internalFire(StateContext<T, S, E, C> stateContext);
	
	void exit(StateContext<T, S, E, C> stateContext);
	
	State<T, S, E, C> transit(StateContext<T, S, E, C> stateContext);
}
