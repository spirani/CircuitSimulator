package com.ra4king.circuitsimulator.simulator;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Roi Atalla
 */
public class Port {
	public final Component component;
	public final int portIndex;
	private Link link;
	
	public Port(Component component, int portIndex, int bitSize) {
		if(component.getCircuit() == null) {
			throw new NullPointerException("Cannot create a Port without a circuit.");
		}
		
		this.component = component;
		this.portIndex = portIndex;
		
		link = new Link(component.getCircuit(), bitSize);
		link.participants.add(this);
	}
	
	public Component getComponent() {
		return component;
	}
	
	public int getPortIndex() {
		return portIndex;
	}
	
	public Link getLink() {
		return link;
	}
	
	public Port linkPort(Port port) {
		link.linkPort(port);
		return this;
	}
	
	public Port unlinkPort(Port port) {
		link.unlinkPort(port);
		return this;
	}
	
	@Override
	public int hashCode() {
		return component.hashCode() ^ portIndex;
	}
	
	@Override
	public boolean equals(Object other) {
		if(other instanceof Port) {
			Port port = (Port)other;
			return port.component == this.component && port.portIndex == this.portIndex;
		}
		
		return false;
	}
	
	@Override
	public String toString() {
		return "Port(" + component + "[" + portIndex + "])";
	}
	
	public static class Link {
		private final Circuit circuit;
		private final Set<Port> participants;
		private final int bitSize;
		
		public Link(Circuit circuit, int bitSize) {
			this.circuit = circuit;
			this.participants = new HashSet<>();
			this.bitSize = bitSize;
		}
		
		public Circuit getCircuit() {
			return circuit;
		}
		
		public int getBitSize() {
			return bitSize;
		}
		
		public Set<Port> getParticipants() {
			return participants;
		}
		
		public Link linkPort(Port port) {
			if(participants.contains(port)) return this;
			
			if(port.getLink().circuit != circuit)
				throw new IllegalArgumentException("Links belong to different circuits.");
			
			if(port.getLink().bitSize != bitSize)
				throw new IllegalArgumentException("Links have different bit sizes.");
			
			circuit.getCircuitStates().forEach(state -> state.link(this, port.link));
			
			Set<Port> portParticipants = port.link.participants;
			participants.addAll(portParticipants);
			
			for(Port p : portParticipants) {
				p.link = this;
			}
			
			return this;
		}
		
		public Link unlinkPort(Port port) {
			if(!participants.contains(port)) return this;
			
			participants.remove(port);
			port.link = new Link(circuit, bitSize);
			port.link.participants.add(port);
			
			circuit.getCircuitStates().forEach(state -> state.unlink(this, port));
			
			return this;
		}
	}
}
