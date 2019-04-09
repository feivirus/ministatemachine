package com.feivirus.ministatemachine;

import java.util.List;

import com.feivirus.ministatemachine.StateMachine;

public interface State<T extends StateMachine<T, S, E, C>, S, E, C> {
	
	S getStateId();
	
	Transition<T, S, E, C> addTransitionOn(E event);
	
	void internalFire(StateContext<T, S, E, C> stateContext);
	
	List<Transition<T, S, E, C>> getTransitions(E event);
	
	void exit(StateContext<T, S, E, C> stateContext);
	
	public void entry(StateContext<T, S, E, C> stateContext);
}
