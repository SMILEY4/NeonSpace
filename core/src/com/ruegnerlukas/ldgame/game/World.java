package com.ruegnerlukas.ldgame.game;



public class World {

	
	private int width;
	private int height;
	private int cellSize;
	private Cell[][] cells;
	
	
	
	

	public World(int width, int height, int cellSize) {
		this.width = width;
		this.height = height;
		this.cellSize = cellSize;
		this.cells = new Cell[width][height];
		for(int x=0; x<width; x++) {
			for(int y=0; y<height; y++) {
				cells[x][y] = new Cell(x, y);
			}
		}
	}


	
	
	
	
	public Cell getCell(int x, int y) {
		if(x<0 || y<0 || x>=width || y>=height) {
			return null;
		}
		return cells[x][y];
	}
	
	
	
	
	public int getWidth() {
		return width;
	}


	
	
	public void setWidth(int width) {
		this.width = width;
	}


	
	
	public int getHeight() {
		return height;
	}


	
	
	public void setHeight(int height) {
		this.height = height;
	}


	
	
	public int getCellSize() {
		return cellSize;
	}


	
	
	public void setCellSize(int cellSize) {
		this.cellSize = cellSize;
	}
	
	
}
