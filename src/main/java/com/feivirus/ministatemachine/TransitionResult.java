package com.feivirus.ministatemachine;

public interface TransitionResult <T extends StateMachine<T, S, E, C>, S, E, C>{
	boolean isAccepted();	
	
	TransitionResult<T, S, E, C> setAccepted(boolean accept);
	
	TransitionResult<T, S, E, C> setTargetState(State<T, S, E, C> targetState);
	
	State<T, S, E, C> getTargetState();
}
