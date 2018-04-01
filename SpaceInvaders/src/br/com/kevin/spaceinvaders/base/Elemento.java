package br.com.kevin.spaceinvaders.base;

import java.awt.Color;
import java.awt.Graphics2D;

public class Elemento {
	private int px;
	private int py;
	private int largura;
	private int altura;
	private int vel;
	private boolean ativo;
	private Color cor;

	public Elemento() {
	}

	public Elemento(int px, int py, int largura, int altura) {
		this.px = px;
		this.py = py;
		this.largura = largura;
		this.altura = altura;
	}

	public void atualiza() {

	}

	public void desenha(Graphics2D g) {
		g.drawRect(this.px, this.py, this.largura, this.altura);
	}

	public void incPx(int x) {
		this.px = this.px + x;
	}

	public void incPy(int y) {
		this.py = this.py + y;
	}

	public int getPx() {
		return this.px;
	}

	public void setPx(int px) {
		this.px = px;
	}

	public int getPy() {
		return this.py;
	}

	public void setPy(int py) {
		this.py = py;
	}

	public int getLargura() {
		return this.largura;
	}

	public void setLargura(int largura) {
		this.largura = largura;
	}

	public int getAltura() {
		return this.altura;
	}

	public void setAltura(int altura) {
		this.altura = altura;
	}

	public int getVel() {
		return this.vel;
	}

	public void setVel(int vel) {
		this.vel = vel;
	}

	public boolean isAtivo() {
		return this.ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public Color getCor() {
		return this.cor;
	}

	public void setCor(Color cor) {
		this.cor = cor;
	}

}
