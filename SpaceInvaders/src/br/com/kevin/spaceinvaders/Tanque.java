package br.com.kevin.spaceinvaders;

import java.awt.Color;
import java.awt.Graphics2D;

import br.com.kevin.spaceinvaders.base.Elemento;

public class Tanque extends Elemento {

	private final int cano = 8;

	private final int escotilha = 10;

	public Tanque() {
		setLargura(30);
		setAltura(15);
	}

	@Override
	public void atualiza() {
	}

	@Override
	public void desenha(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect((getPx() + (getLargura() / 2)) - (this.cano / 2), getPy() - this.cano, this.cano, this.cano);

		g.fillRect(getPx(), getPy(), getLargura(), getAltura());

		g.setColor(Color.YELLOW);
		g.fillOval((getPx() + (getLargura() / 2)) - (this.escotilha / 2), (getPy() + (getAltura() / 2)) - (this.escotilha / 2),
				this.escotilha, this.escotilha);
	}

}