package com.ra4king.circuitsimulator.simulator.tests;

import com.ra4king.circuitsimulator.simulator.Circuit;
import com.ra4king.circuitsimulator.simulator.Simulator;
import com.ra4king.circuitsimulator.simulator.WireValue;
import com.ra4king.circuitsimulator.simulator.components.Adder;
import com.ra4king.circuitsimulator.simulator.components.Clock;
import com.ra4king.circuitsimulator.simulator.components.Pin;
import com.ra4king.circuitsimulator.simulator.components.Register;

/**
 * @author Roi Atalla
 */
public class ClockTest {
	public static void main(String[] args) {
		Simulator simulator = new Simulator();
		Circuit circuit = new Circuit(simulator);
		
		Clock clock = circuit.addComponent(new Clock(""));
		Register register = circuit.addComponent(new Register("", 32));
		Adder adder = circuit.addComponent(new Adder("", 32));
		Pin din = circuit.addComponent(new Pin("Din", 32, true));
		Pin cin = circuit.addComponent(new Pin("Cin", 1, true));
		Pin out = circuit.addComponent(new Pin("Out", 32, false));
		
		register.getPort(Register.PORT_IN).linkPort(adder.getPort(Adder.PORT_OUT));
		register.getPort(Register.PORT_CLK).linkPort(clock.getPort(Clock.PORT));
		adder.getPort(Adder.PORT_A).linkPort(register.getPort(Register.PORT_OUT)).linkPort(out.getPort(Pin.PORT));
		adder.getPort(Adder.PORT_B).linkPort(din.getPort(Pin.PORT));
		adder.getPort(Adder.PORT_CARRY_IN).linkPort(cin.getPort(Pin.PORT));
		
		din.setValue(circuit.getTopLevelState(), WireValue.of(1, 32));
		cin.setValue(circuit.getTopLevelState(), WireValue.of(0, 1));
		
		Clock.startClock(4);
		
		while(true) {
			simulator.step();
		}
	}
}
