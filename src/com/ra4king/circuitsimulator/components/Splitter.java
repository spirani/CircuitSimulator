package com.ra4king.circuitsimulator.components;

import com.ra4king.circuitsimulator.Component;
import com.ra4king.circuitsimulator.Simulator;
import com.ra4king.circuitsimulator.WireValue;

/**
 * @author Roi Atalla
 */
public class Splitter extends Component {
	public Splitter(Simulator simulator, String name, int bitSize, int fanouts) {
		this(simulator, name, setupBitFanIndices(bitSize, fanouts));
	}
	
	public Splitter(Simulator simulator, String name, int[] bitFanIndices) {
		super(simulator, name, setupPortBitsizes(bitFanIndices));
		properties.put(PropertyType.SPLITTER_BITS_PER_FAN, bitFanIndices);
	}
	
	private static int[] setupBitFanIndices(int bitSize, int fanouts) {
		int numBitsPerFan = bitSize / fanouts;
		if((bitSize % fanouts) != 0)
			numBitsPerFan++;
		
		int[] bitFanIndices = new int[bitSize];
		for(int i = 0; i < bitSize; i++) {
			bitFanIndices[i] = i / numBitsPerFan;
		}
		
		return bitFanIndices;
	}
	
	private static int[] setupPortBitsizes(int[] bitFanIndices) {
		int totalFans = 0;
		for(int bitFanIdx : bitFanIndices) {
			if(bitFanIdx > totalFans)
				totalFans = bitFanIdx;
		}
		
		if(totalFans == 0) {
			throw new IllegalArgumentException("Must have at least one bit going to a fanout.");
		}
		
		int[] fanouts = new int[totalFans + 2];
		fanouts[totalFans + 1] = bitFanIndices.length;
		
		for(int bitFanIndex : bitFanIndices) {
			if(bitFanIndex >= 0)
				fanouts[bitFanIndex]++;
		}
		
		return fanouts;
	}
	
	@Override
	public void valueChanged(WireValue value, int portIndex) {
		int[] bitFanIndices = (int[])properties.get(PropertyType.SPLITTER_BITS_PER_FAN);
		
		if(bitFanIndices.length != value.getBitSize()) {
			throw new IllegalStateException(this + ": something went wrong somewhere. bitFanIndices = " + bitFanIndices.length + ", value.getBitSize() = " + value.getBitSize());
		}
		
		if(portIndex == ports.length - 1) {
			for(int i = 0; i < ports.length - 1; i++) {
				WireValue result = new WireValue(ports[i].getWireValue().getBitSize());
				int currBit = 0;
				for(int j = 0; j < bitFanIndices.length; j++) {
					if(bitFanIndices[j] == i) {
						result.setBit(currBit++, value.getBit(j));
					}
				}
				ports[i].pushValue(result);
			}
		} else {
			WireValue result = new WireValue(ports[ports.length - 1].getWireValue());
			int currBit = 0;
			for(int i = 0; i < bitFanIndices.length; i++) {
				if(bitFanIndices[i] == portIndex) {
					result.setBit(i, value.getBit(currBit++));
				}
			}
			ports[ports.length - 1].pushValue(result);
		}
	}
}