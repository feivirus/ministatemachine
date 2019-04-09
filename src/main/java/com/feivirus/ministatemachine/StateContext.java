package com.feivirus.ministatemachine;

public interface StateContext <T extends StateMachine<T, S, E, C>, S, E, C>{
	TransitionResult<T, S, E, C> getResult();
	
	E getEvent();
	
	State<T, S, E, C> getFromState();
	
	ActionExecutionService<T, S, E, C> getExecutor();
	
	StateMachine<T, S, E, C> getStateMachine();
	
	C getContext();
}
