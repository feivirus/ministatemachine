package com.feivirus.ministatemachine.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.feivirus.ministatemachine.Action;
import com.feivirus.ministatemachine.State;
import com.feivirus.ministatemachine.StateContext;
import com.feivirus.ministatemachine.StateMachine;
import com.feivirus.ministatemachine.Transition;
import com.feivirus.ministatemachine.TransitionResult;

public class StateImpl<T extends StateMachine<T, S, E, C>, S, E, C> implements State<T, S, E, C>{
	protected S stateId;
	
	protected Map<E, Transition<T, S, E, C>> transitions = new HashMap<>();
	
	private boolean isFinalState;
	
	private List<Action<T, S, E, C>> entryActions = new ArrayList<>();
	
	private List<Action<T, S, E, C>> exitActions = new ArrayList<>();
	
	public StateImpl(S stateId) {
		this.stateId = stateId;
	}
	
	@Override
	public S getStateId() {
		return stateId;
	}

	@Override
	public Transition<T, S, E, C> addTransitionOn(E event) {
		Transition<T, S, E, C> newTransition = FSM.newTransition();
		newTransition.setSourceState(this);
		newTransition.setEvent(event);
		transitions.put(event, newTransition);
		return newTransition;
	}

	@Override
	public void internalFire(StateContext<T, S, E, C> stateContext) {
		TransitionResult<T, S, E, C> transitionResult = stateContext.getResult();
		List<Transition<T, S, E, C>> transitions = getTransitions(stateContext.getEvent());
		
		//目标只支持现态下面只有一个次态
		for(Transition<T, S, E, C> transition : transitions) {
			transition.internalFire(stateContext);
		}
	}

	@Override
	public List<Transition<T, S, E, C>> getTransitions(E event) {
		if (transitions == null) {
			return Collections.emptyList();
		}
		List<Transition<T, S, E, C>> result = new ArrayList<>();
		result.add(transitions.get(event));
		return result;		
	}

	@Override
	public void exit(StateContext<T, S, E, C> stateContext) {
		if (isFinalState) {
			return;
		}	
		
		for(Action<T, S, E, C> exitAction : exitActions) {
			stateContext.getExecutor().defer(exitAction, getStateId(), 
					null, stateContext.getEvent(), stateContext.getContext(), 
					stateContext.getStateMachine().getThis());
		}
	}

	@Override
	public void entry(StateContext<T, S, E, C> stateContext) {
		
	}
}
