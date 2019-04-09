package com.feivirus.ministatemachine.impl;

import com.feivirus.ministatemachine.ActionExecutionService;
import com.feivirus.ministatemachine.State;
import com.feivirus.ministatemachine.StateContext;
import com.feivirus.ministatemachine.StateMachine;
import com.feivirus.ministatemachine.StateMachineData;
import com.feivirus.ministatemachine.TransitionResult;

public class StateContextImpl<T extends StateMachine<T, S, E, C>, S, E, C> implements StateContext<T, S, E, C>{
	private StateMachine<T, S, E, C> stateMachine;
	
	private StateMachineData<T, S, E, C> stateMachineData;
	
	private State<T, S, E, C> fromState;
	
	private C context;
	
	private E event;
	
	private TransitionResult<T, S, E, C> result;
	
	private ActionExecutionService<T, S, E, C> executor;
	
	public StateContextImpl(StateMachine<T, S, E, C> stateMachine, StateMachineData<T, S, E, C> stateMachineData,
			State<T, S, E, C> fromState, E event, C context,
			TransitionResult<T, S, E, C> result, ActionExecutionService<T, S, E, C> executor) {
		this.stateMachine = stateMachine;		
		this.stateMachineData = stateMachineData;
		this.fromState = fromState;
		this.context = context;
		this.event = event;
		this.result = result;
		this.executor = executor;
	}

	@Override
	public TransitionResult<T, S, E, C> getResult() {
		return result;
	}

	@Override
	public E getEvent() {
		return event;
	}

	@Override
	public State<T, S, E, C> getFromState() {
		return fromState;
	}

	@Override
	public ActionExecutionService<T, S, E, C> getExecutor() {
		return executor;
	}

	@Override
	public StateMachine<T, S, E, C> getStateMachine() {
		return stateMachine;
	}

	@Override
	public C getContext() {
		return context;
	}	
}
