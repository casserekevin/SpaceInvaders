package br.com.kevin.spaceinvaders;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

import br.com.kevin.spaceinvaders.Invader.Tipos;
import br.com.kevin.spaceinvaders.base.Elemento;
import br.com.kevin.spaceinvaders.base.Texto;
import br.com.kevin.spaceinvaders.base.Util;

@SuppressWarnings("serial")
public class Jogo extends JFrame {

	private static final int FPS = 1000 / 20;

	private static final int JANELA_ALTURA = 680;

	private static final int JANELA_LARGURA = 540;

	private JPanel tela;

	private Graphics2D g2d;

	private BufferedImage buffer;

	private boolean[] controleTecla = new boolean[5];

	public Jogo() {
		addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				setaTecla(e.getKeyCode(), false);
			}

			@Override
			public void keyPressed(KeyEvent e) {
				setaTecla(e.getKeyCode(), true);
			}
		});

		this.buffer = new BufferedImage(JANELA_LARGURA, JANELA_ALTURA, BufferedImage.TYPE_INT_RGB);

		this.g2d = this.buffer.createGraphics();

		this.tela = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g) {
				g.drawImage(Jogo.this.buffer, 0, 0, null);
			}
		};

		getContentPane().add(this.tela);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setSize(JANELA_LARGURA, JANELA_ALTURA);
		setVisible(true);
	}

	private void setaTecla(int tecla, boolean pressionada) {
		switch (tecla) {
			case KeyEvent.VK_UP:
				this.controleTecla[0] = pressionada;
				break;
			case KeyEvent.VK_DOWN:
				this.controleTecla[1] = pressionada;
				break;
			case KeyEvent.VK_LEFT:
				this.controleTecla[2] = pressionada;
				break;
			case KeyEvent.VK_RIGHT:
				this.controleTecla[3] = pressionada;
				break;
			case KeyEvent.VK_SPACE:
				this.controleTecla[4] = pressionada;
				break;
		}
	}

	// Elementos do jogo

	private int vidas = 3;

	// Desenharemos mais dois tanques na base da tela
	private Elemento vida = new Tanque();

	private Elemento tiroTanque;

	private Elemento tiroChefe;

	private Elemento[] tiros = new Tiro[3];

	private Texto texto = new Texto();

	private Invader chefe;

	private Elemento tanque;

	private Invader[][] invasores = new Invader[11][5];

	private Invader.Tipos[] tipoPorLinha = {Tipos.PEQUENO, Tipos.MEDIO, Tipos.MEDIO, Tipos.GRANDE, Tipos.GRANDE};

	//
	private int linhaBase = 60;

	// Controle do espacamento entre os inimigos e outros elementos
	private int espacamento = 15;

	// Contador de inimigos destruidos
	private int destruidos = 0;

	private int dir;

	private int totalInimigos;

	private int contadorEspera;

	boolean novaLinha;

	boolean moverInimigos;

	private int contador;

	private int pontos;

	private int level = 1;

	private Random rand = new Random();

	private void carregarJogo() {

		this.tanque = new Tanque();
		this.tanque.setVel(5);
		this.tanque.setAtivo(true);
		this.tanque.setPx((this.tela.getWidth() / 2) - (this.tanque.getLargura() / 2));
		this.tanque.setPy(this.tela.getHeight() - this.tanque.getAltura() - this.linhaBase);

		this.tiroTanque = new Tiro();
		this.tiroTanque.setVel(-20);

		this.chefe = new Invader(Invader.Tipos.CHEFE);

		this.tiroChefe = new Tiro(true);
		this.tiroChefe.setVel(20);
		this.tiroChefe.setAltura(15);

		for (int i = 0; i < this.tiros.length; i++) {
			this.tiros[i] = new Tiro(true);
		}

		for (int i = 0; i < this.invasores.length; i++) {
			for (int j = 0; j < this.invasores[i].length; j++) {
				Invader e = new Invader(this.tipoPorLinha[j]);

				e.setAtivo(true);

				e.setPx((i * e.getLargura()) + ((i + 1) * this.espacamento));
				e.setPy((j * e.getAltura()) + (j * this.espacamento) + this.linhaBase);

				this.invasores[i][j] = e;
			}
		}

		this.dir = 1;

		this.totalInimigos = this.invasores.length * this.invasores[0].length;

		this.contadorEspera = this.totalInimigos / this.level;
	}

	public void iniciarJogo() {
		long prxAtualizacao = 0;

		while (true) {
			if (System.currentTimeMillis() >= prxAtualizacao) {

				this.g2d.setColor(Color.BLACK);
				this.g2d.fillRect(0, 0, JANELA_LARGURA, JANELA_ALTURA);

				if (this.destruidos == this.totalInimigos) {
					this.destruidos = 0;
					this.level++;
					carregarJogo();

					continue;
				}

				if (this.contador > this.contadorEspera) {
					this.moverInimigos = true;
					this.contador = 0;
					this.contadorEspera = this.totalInimigos - this.destruidos - (this.level * this.level);

				}
				else {
					this.contador++;
				}

				if (this.tanque.isAtivo()) {
					if (this.controleTecla[2]) {
						this.tanque.setPx(this.tanque.getPx() - this.tanque.getVel());

					}
					else if (this.controleTecla[3]) {
						this.tanque.setPx(this.tanque.getPx() + this.tanque.getVel());
					}
				}

				// Pressionou espaco, adiciona tiro
				if (this.controleTecla[4] && !this.tiroTanque.isAtivo()) {
					this.tiroTanque.setPx((this.tanque.getPx() + (this.tanque.getLargura() / 2)) - (this.tiroTanque.getLargura() / 2));
					this.tiroTanque.setPy(this.tanque.getPy() - this.tiroTanque.getAltura());
					this.tiroTanque.setAtivo(true);
				}

				if (this.chefe.isAtivo()) {
					this.chefe.incPx(this.tanque.getVel() - 1);

					if (!this.tiroChefe.isAtivo() && Util.colideX(this.chefe, this.tanque)) {
						addTiroInimigo(this.chefe, this.tiroChefe);
					}

					if (this.chefe.getPx() > this.tela.getWidth()) {
						this.chefe.setAtivo(false);
					}
				}

				boolean colideBordas = false;

				// Percorrendo primeiro as linhas, de baixo para cima
				for (int j = this.invasores[0].length - 1; j >= 0; j--) {

					// Depois as colunas
					for (int i = 0; i < this.invasores.length; i++) {

						Invader inv = this.invasores[i][j];

						if (!inv.isAtivo()) {
							continue;
						}

						if (Util.colide(this.tiroTanque, inv)) {
							inv.setAtivo(false);
							this.tiroTanque.setAtivo(false);

							this.destruidos++;
							this.pontos = this.pontos + (inv.getPremio() * this.level);

							continue;
						}

						if (this.moverInimigos) {

							inv.atualiza();

							if (this.novaLinha) {
								inv.setPy(inv.getPy() + inv.getAltura() + this.espacamento);
							}
							else {
								inv.incPx(this.espacamento * this.dir);
							}

							if (!this.novaLinha && !colideBordas) {
								int pxEsq = inv.getPx() - this.espacamento;
								int pxDir = inv.getPx() + inv.getLargura() + this.espacamento;

								if ((pxEsq <= 0) || (pxDir >= this.tela.getWidth())) {
									colideBordas = true;
								}
							}

							if (!this.tiros[0].isAtivo() && (inv.getPx() < this.tanque.getPx())) {
								addTiroInimigo(inv, this.tiros[0]);

							}
							else if (!this.tiros[1].isAtivo() && (inv.getPx() > this.tanque.getPx())
									&& (inv.getPx() < (this.tanque.getPx() + this.tanque.getLargura()))) {
								addTiroInimigo(inv, this.tiros[1]);

							}
							else if (!this.tiros[2].isAtivo() && (inv.getPx() > this.tanque.getPx())) {
								addTiroInimigo(inv, this.tiros[2]);

							}

							if (!this.chefe.isAtivo() && (this.rand.nextInt(500) == this.destruidos)) {
								this.chefe.setPx(0);
								this.chefe.setAtivo(true);

							}

						}

						// Desenhe aqui se quiser economizar no loop.
						// e.desenha(g2d);

					}
				}

				if (this.moverInimigos && this.novaLinha) {
					this.dir *= -1;
					this.novaLinha = false;

				}
				else if (this.moverInimigos && colideBordas) {
					this.novaLinha = true;
				}

				this.moverInimigos = false;

				if (this.tiroTanque.isAtivo()) {
					this.tiroTanque.incPy(this.tiroTanque.getVel());

					if (Util.colide(this.tiroTanque, this.chefe)) {
						this.pontos = this.pontos + (this.chefe.getPremio() * this.level);
						this.chefe.setAtivo(false);
						this.tiroTanque.setAtivo(false);

					}
					else if (this.tiroTanque.getPy() < 0) {
						this.tiroTanque.setAtivo(false);
					}

					this.tiroTanque.desenha(this.g2d);
				}

				if (this.tiroChefe.isAtivo()) {
					this.tiroChefe.incPy(this.tiroChefe.getVel());

					if (Util.colide(this.tiroChefe, this.tanque)) {
						this.vidas--;
						this.tiroChefe.setAtivo(false);

					}
					else if (this.tiroChefe.getPy() > (this.tela.getHeight() - this.linhaBase - this.tiroChefe.getAltura())) {
						this.tiroChefe.setAtivo(false);
					}
					else {
						this.tiroChefe.desenha(this.g2d);
					}

				}

				for (int i = 0; i < this.tiros.length; i++) {
					if (this.tiros[i].isAtivo()) {
						this.tiros[i].incPy(+10);

						if (Util.colide(this.tiros[i], this.tanque)) {
							this.vidas--;
							this.tiros[i].setAtivo(false);

						}
						else if (this.tiros[i].getPy() > (this.tela.getHeight() - this.linhaBase - this.tiros[i].getAltura())) {
							this.tiros[i].setAtivo(false);
						}

						this.tiros[i].desenha(this.g2d);
					}
				}

				// Desenhe aqui para as naves ficarem acima dos tiros
				for (int i = 0; i < this.invasores.length; i++) {
					for (int j = 0; j < this.invasores[i].length; j++) {
						Invader e = this.invasores[i][j];
						e.desenha(this.g2d);
					}
				}

				this.tanque.atualiza();
				this.tanque.desenha(this.g2d);

				this.chefe.atualiza();
				this.chefe.desenha(this.g2d);

				this.g2d.setColor(Color.WHITE);

				this.texto.desenha(this.g2d, String.valueOf(this.pontos), 10, 20);
				this.texto.desenha(this.g2d, "Level " + this.level, this.tela.getWidth() - 100, 20);
				this.texto.desenha(this.g2d, String.valueOf(this.vidas), 10, this.tela.getHeight() - 10);

				// Linha base
				this.g2d.setColor(Color.GREEN);
				this.g2d.drawLine(0, this.tela.getHeight() - this.linhaBase, this.tela.getWidth(), this.tela.getHeight() - this.linhaBase);

				for (int i = 1; i < this.vidas; i++) {
					this.vida.setPx((i * this.vida.getLargura()) + (i * this.espacamento));
					this.vida.setPy(this.tela.getHeight() - this.vida.getAltura());

					this.vida.desenha(this.g2d);
				}

				this.tela.repaint();

				prxAtualizacao = System.currentTimeMillis() + FPS;
			}
		}

	}

	public void addTiroInimigo(Elemento inimigo, Elemento tiro) {
		tiro.setAtivo(true);
		tiro.setPx((inimigo.getPx() + (inimigo.getLargura() / 2)) - (tiro.getLargura() / 2));
		tiro.setPy(inimigo.getPy() + inimigo.getAltura());
	}

	public static void main(String[] args) {
		Jogo jogo = new Jogo();
		jogo.carregarJogo();
		jogo.iniciarJogo();

	}

}
