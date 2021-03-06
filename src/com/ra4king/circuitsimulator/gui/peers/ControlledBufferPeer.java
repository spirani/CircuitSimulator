package com.ra4king.circuitsimulator.gui.peers;

import java.util.ArrayList;
import java.util.List;

import com.ra4king.circuitsimulator.gui.ComponentPeer;
import com.ra4king.circuitsimulator.gui.Connection;
import com.ra4king.circuitsimulator.gui.Connection.PortConnection;
import com.ra4king.circuitsimulator.gui.GuiUtils;
import com.ra4king.circuitsimulator.simulator.CircuitState;
import com.ra4king.circuitsimulator.simulator.components.ControlledBuffer;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * @author Roi Atalla
 */
public class ControlledBufferPeer extends ComponentPeer<ControlledBuffer> {
	private List<Connection> connections = new ArrayList<>();
	
	public ControlledBufferPeer(ControlledBuffer buffer, int x, int y) {
		super(buffer, x, y, GuiUtils.BLOCK_SIZE * 2, GuiUtils.BLOCK_SIZE * 2);
		
		connections.add(new PortConnection(this, buffer.getPort(ControlledBuffer.PORT_IN), GuiUtils.BLOCK_SIZE, 0));
		connections.add(new PortConnection(this, buffer.getPort(ControlledBuffer.PORT_ENABLE), 0, GuiUtils.BLOCK_SIZE));
		connections.add(new PortConnection(this, buffer.getPort(ControlledBuffer.PORT_OUT), GuiUtils.BLOCK_SIZE, getHeight()));
	}
	
	@Override
	public List<Connection> getConnections() {
		return connections;
	}
	
	@Override
	public void paint(GraphicsContext graphics, CircuitState circuitState) {
		graphics.setFill(Color.WHITE);
		GuiUtils.drawShape(graphics::fillRect, this);
		graphics.setStroke(Color.BLACK);
		GuiUtils.drawShape(graphics::strokeRect, this);
	}
}
