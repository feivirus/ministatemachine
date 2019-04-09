package com.feivirus.ministatemachine;

import java.io.Serializable;

public interface StateMachineData <T extends StateMachine<T, S, E, C>, S, E, C> extends Serializable{
	void dump(StateMachineData.Reader<T, S, E, C> src);
	
	Reader<T, S, E, C> read();
	
	Writer<T, S, E, C> write();
	
	public interface Reader<T extends StateMachine<T, S, E, C>, S, E, C> extends Serializable {
		//从用户定义的状态S返回状态机的接口StateImpl
		State<T, S, E, C> stateFromUser(S stateId);
		
		State<T, S, E, C> currentStateFromUser();
		
		State<T, S, E, C> initialStateFromUser();
		
		S currentState();
		
		S initialState();
	}
	
	public interface Writer<T extends StateMachine<T, S, E, C>, S, E, C> extends Serializable {
		void initialState(S stateId);
		
		void currentState(S stateId);
	}
}
