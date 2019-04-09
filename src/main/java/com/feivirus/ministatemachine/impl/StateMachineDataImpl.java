package com.feivirus.ministatemachine.impl;

import java.util.Collections;
import java.util.Map;

import com.feivirus.ministatemachine.State;
import com.feivirus.ministatemachine.StateMachine;
import com.feivirus.ministatemachine.StateMachineData;

public class StateMachineDataImpl<T extends StateMachine<T, S, E, C>, S, E, C> implements StateMachineData<T, S, E, C>, 
		StateMachineData.Writer<T, S, E, C>, StateMachineData.Reader<T, S, E, C>{
		
	private static final long serialVersionUID = -6420447220576347182L;
	
	private Map<S, State<T, S, E, C>> states;
	
	private S initialState;
	
	private S currentState;
	
	public StateMachineDataImpl(Map<S, ? extends State<T, S, E, C>> states) {		
		this.states = Collections.unmodifiableMap(states);		
	}

	@Override
	public void dump(StateMachineData.Reader<T, S, E, C> src) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public StateMachineData.Reader<T, S, E, C> read() {		
		return this;
	}

	@Override
	public StateMachineData.Writer<T, S, E, C> write() {		
		return this;
	}	

	@Override
	public void initialState(S stateId) {		
		this.initialState = stateId;
	}

	@Override
	public void currentState(S stateId) {
		this.currentState = stateId;
	}

	@Override
	public S currentState() {
		return currentState;
	}

	@Override
	public S initialState() {
		return initialState;
	}

	@Override
	public State<T, S, E, C> stateFromUser(S stateId) {
		if (stateId == null) {
			return null;
		}
		State<T, S, E, C> state = states.get(stateId);
		return state;
	}

	@Override
	public State<T, S, E, C> currentStateFromUser() {
		return stateFromUser(currentState);
	}

	@Override
	public State<T, S, E, C> initialStateFromUser() {
		return stateFromUser(initialState);
	}	
}
