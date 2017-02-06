/*   
 *   Super Mario Propulsion
 *   Copyright (C) Este programa é software livre; você pode redistribuí-lo e/ou
 *   modificá-lo sob os termos da Licença Pública Geral GNU, conforme
 *   publicada pela Free Software Foundation; tanto a versão 2 da
 *   Licença como (a seu critério) qualquer versão mais nova.
 *
 *   Este programa é distribuído na expectativa de ser útil, mas SEM
 *   QUALQUER GARANTIA; sem mesmo a garantia implícita de
 *   COMERCIALIZAÇÃO ou de ADEQUAÇÃO A QUALQUER PROPÓSITO EM
 *   PARTICULAR. Consulte a Licença Pública Geral GNU para obter mais
 *   detalhes.
 *
 *   Você deve ter recebido uma cópia da Licença Pública Geral GNU
 *   junto com este programa; se não, escreva para a Free Software
 *   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 *   02111-1307, USA.
 */

package intro;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.Timer;

import etc.Animacao;
import etc.MarioAnimacao;
import etc.Temporizador;

import jogo.SMPropulsion;

public class Introducao extends KeyAdapter
{
	private final String IMAGE_DIR = "img/intro/";
	private SMPropulsion jogo;
	private BufferStrategy bufferStrategy;
	private boolean ativa, viuCena4 = false;
	
	public Introducao(SMPropulsion jogo, BufferStrategy bs)
	{
		this.jogo = jogo;
		this.bufferStrategy = bs;
		this.jogo.addKeyListener(this);
	}
	
	public void exibir()
	{
		this.ativa = true;
		
		new Cena1().atualizar();
		new Cena2().loop();
		
		MarioAnimacao mario = new MarioAnimacao(400, this.jogo.getAlturaDaTela() - 20, 150);
		mario.iniciarAnimacao();
		
		if(this.ativa) new Cena3(mario).loop();
		if(this.ativa) new Cena4(mario).loop();
		if(this.viuCena4) new Cena5(mario).loop();
		
		this.jogo.removeKeyListener(this);
	}

	private class Cena1 implements Animacao //"plim!"
	{	
		public void atualizar() {
			File somDeMoeda =  new File("sound/wav/coin.wav");
			ImageIcon icon1 = new ImageIcon("img/intro/intro1.gif");

			Introducao.this.fixarImagem(null, 1);
			Introducao.this.jogo.reproduzirEfeitoSonoro(somDeMoeda);
			Introducao.this.fixarImagem(icon1, 2);
		}
		
		public void loop(){}
		public void desenhar(Graphics2D gScr){}
	}
	
	private class Cena2 implements Animacao //mario anda e pula
	{
		private MarioAnimacao mario;
		private boolean pulou = false, bateu = false;
		private ImageIcon icon1;
		private int yIcon = 0;
		
		public void loop() {
			Graphics2D gScr = (Graphics2D) jogo.obterContextoGrafico();
			
			this.icon1 = new ImageIcon("img/intro/intro1.gif");
			this.mario = new MarioAnimacao();
			Temporizador temp = new Temporizador(this, 13);
			
			while(mario.ativo() && Introducao.this.ativa)
			{
				this.atualizar();
				this.desenhar(gScr);
				temp.normalizar();
				temp.pularQuadros();
			}
			
			gScr.dispose();
		}
		
		public void atualizar() {
			mario.x(mario.x() + 2);

			if(mario.x() >= 172 && mario.x() < 192){
				if(!pulou) {
					Introducao.this.jogo.reproduzirEfeitoSonoro(new File("sound/wav/jump.wav"));
					pulou = true;
				}
			} else if(mario.x() >= 192 && mario.x() < 242){
				mario.pararAnimacao();
				mario.imagemAtual(2);
				mario.y(mario.y() - 2);
				
				if(mario.x() >= 202 && mario.x() < 207)
					if(!bateu) {
						Introducao.this.jogo.reproduzirEfeitoSonoro(new File("sound/wav/stomp.wav"));
						bateu = true;
					}
	
				if(mario.x() >= 187 && mario.x() < 192) yIcon += 2;
				
			} else if(mario.x() >= 242 && mario.x() < 292){
				if(mario.x() >= 242 && mario.x() < 247) icon1 = null;
				mario.imagemAtual(3);
				mario.y(mario.y() + 2);			
			} else if(mario.x() >= 292 && mario.x() < 512)
				mario.iniciarAnimacao();
			else if(mario.x() >= 512)
				mario.ativo(false);
		}

		public void desenhar(Graphics2D gScr) {
			gScr.setColor(Color.BLACK);
			gScr.fillRect(0, 0, Introducao.this.jogo.getLarguraDaTela(), Introducao.this.jogo.getAlturaDaTela());
			if(icon1 != null) gScr.drawImage(icon1.getImage(), 0, yIcon, null);
			else gScr.fillRect(0, 0, Introducao.this.jogo.getLarguraDaTela(), Introducao.this.jogo.getAlturaDaTela());
			
			mario.desenhar(gScr);
			Introducao.this.mostrar();
		}
	}
	
	private class Cena3 implements Animacao //imagem subindo //inicia musica
	{
		private MarioAnimacao mario;
		private ImageIcon img1, img2, img3, logo;
		int	posY1, posY2, posY3, posYicon, vel = 2;
		
		public Cena3(MarioAnimacao mario) {
			this.mario = mario;
		}
		
		public void loop() {
			this.img1 = new ImageIcon(IMAGE_DIR + "intro2.gif");
			this.img2 = new ImageIcon(IMAGE_DIR + "intro3.gif");
			this.img3 = new ImageIcon(IMAGE_DIR + "intro4.gif");
			this.logo = new ImageIcon(IMAGE_DIR + "logo.png");
					
			Introducao.this.jogo.reproduzirMusica(new File("sound/midi/intro.mid"));
			
			this.posY1 = -img1.getIconHeight();
			this.posY2 = posY1 + -img2.getIconHeight();
			this.posY3 = posY2 + -img3.getIconHeight();
			this.posYicon = Introducao.this.jogo.getAlturaDaTela();
			this.vel = 2;
			
			Graphics2D gScr = (Graphics2D) jogo.obterContextoGrafico();
					
			Temporizador temp = new Temporizador(this, 8);
			
			while(posY3 <= 0 && Introducao.this.ativa) {
				this.atualizar();
				this.desenhar(gScr);
				
				Introducao.this.mostrar();
				temp.normalizar();
				temp.pularQuadros();
			}
			
			gScr.dispose();
		}
		
		public void atualizar() {
			posY1 += vel;
			posY2 += vel;
			posY3 += vel;
			
			if(posY3 >= -700 && posY3 < -520){
				posYicon -= 3;
				mario.y(mario.y() - 3);
			}
		}

		public void desenhar(Graphics2D gScr) {
			gScr.setColor(Color.BLACK);
			gScr.fillRect(0, 0, Introducao.this.jogo.getLarguraDaTela(), Introducao.this.jogo.getAlturaDaTela());
			
			img1.paintIcon(Introducao.this.jogo, gScr, 0, posY1);
			img2.paintIcon(Introducao.this.jogo, gScr, 0, posY2);
			img3.paintIcon(Introducao.this.jogo, gScr, 0, posY3);
			logo.paintIcon(Introducao.this.jogo, gScr, 40, posYicon);
			mario.desenhar(gScr);
			Introducao.this.jogo.desenharTarjas(gScr);
		}
	}
	
	private class Cena4 implements Animacao, ActionListener //"pressione enter"
	{
		private MarioAnimacao mario;
		private ImageIcon img1, logo, enter;
		private int posYimg1, posYimg2, posYicon, vel;
		private boolean mostrar = true;
		
		public Cena4(MarioAnimacao mario) {
			this.mario = mario;
		}
		
		public void loop() {
			Graphics2D gScr = (Graphics2D) jogo.obterContextoGrafico();
			Temporizador temp = new Temporizador(this, 8);
			
			Introducao.this.viuCena4 = true;
			img1 = new ImageIcon(IMAGE_DIR + "intro4.gif");
			logo = new ImageIcon(IMAGE_DIR + "logo.png");
			enter = new ImageIcon(IMAGE_DIR + "enter.png");
			posYimg1 = 0;
			posYimg2 = -img1.getIconHeight();
			posYicon = 130;
			vel = 2;

			new Timer(600, this).start();
									
			while(Introducao.this.ativa){
				this.atualizar();
				this.desenhar(gScr);

				Introducao.this.mostrar();
				temp.normalizar();
				temp.pularQuadros();
			}
			
			gScr.dispose();
		}
		
		public void atualizar() {		
			posYimg1 += vel;
			posYimg2 += vel;
					
			if(posYimg1 > img1.getIconHeight()) posYimg1 = 0;
			if(posYimg2 > 0) posYimg2 = posYimg1 - img1.getIconHeight();
		}

		public void desenhar(Graphics2D gScr) {
			gScr.setColor(Color.BLACK);
			gScr.fillRect(0, 0, Introducao.this.jogo.getLarguraDaTela(), Introducao.this.jogo.getAlturaDaTela());
			
			img1.paintIcon(Introducao.this.jogo, gScr, 0, posYimg1);
			img1.paintIcon(Introducao.this.jogo, gScr, 0, posYimg2);
			logo.paintIcon(Introducao.this.jogo, gScr, 40, posYicon);
			this.mario.desenhar(gScr);
			if(this.mostrar) enter.paintIcon(Introducao.this.jogo, gScr, 180, 260);
			Introducao.this.jogo.desenharTarjas(gScr);
		}

		public void actionPerformed(ActionEvent e) {
			this.mostrar = !this.mostrar;
		}
	}
	
	private class Cena5 implements Animacao //mario vira cursor
	{
		private MarioAnimacao mario;
		
		private ImageIcon img1, logo, iniciar, records;
		private int posYimg1, posYimg2, posYicon, vel, count = 0;
		private boolean movendo = true;
			
		public Cena5(MarioAnimacao mario) {
			this.mario = mario;
		}
		
		public void loop() {
			Graphics2D gScr = (Graphics2D) jogo.obterContextoGrafico();
			Temporizador temp = new Temporizador(this, 8);
			
			this.img1 = new ImageIcon(IMAGE_DIR + "intro4.gif");
			this.logo = new ImageIcon(IMAGE_DIR + "logo.png");
			this.iniciar = new ImageIcon(IMAGE_DIR + "iniciar.png");
			this.records = new ImageIcon(IMAGE_DIR + "records.png");
			this.posYimg1 = 0;
			this.posYimg2 = -img1.getIconHeight();
			this.posYicon = 130;
			this.vel = 2;
			
			this.mario.pararAnimacao();
			this.mario.imagemAtual(12);
			
			while(this.movendo){
				this.atualizar();
				this.desenhar(gScr);
				
				Introducao.this.mostrar();
				temp.normalizar();
				temp.pularQuadros();
			}
			
			gScr.dispose();
		}
		
		public void atualizar() {
			if(count < 70){
				mario.x(mario.x() - 3);
				mario.y(mario.y() + 2);
			}
			else movendo = false;
			
			posYimg1 += vel;
			posYimg2 += vel;
			++count;
					
			if(posYimg1 > img1.getIconHeight()) posYimg1 = 0;
			if(posYimg2 > 0) posYimg2 = posYimg1 - img1.getIconHeight();
		}

		public void desenhar(Graphics2D gScr) {
			gScr.setColor(Color.BLACK);
			gScr.fillRect(0, 0, Introducao.this.jogo.getLarguraDaTela(), Introducao.this.jogo.getAlturaDaTela());
			
			img1.paintIcon(Introducao.this.jogo, gScr, 0, posYimg1);
			img1.paintIcon(Introducao.this.jogo, gScr, 0, posYimg2);
			logo.paintIcon(Introducao.this.jogo, gScr, 40, posYicon);
			iniciar.paintIcon(Introducao.this.jogo, gScr, 260, 360);
			records.paintIcon(Introducao.this.jogo, gScr, 260, 380);
			mario.desenhar(gScr);
			Introducao.this.jogo.desenharTarjas(gScr);
		}
	}
	
	private void fixarImagem(ImageIcon img, int duracao)
	{
		int DURACAO = duracao; //em segundos
		
		long timeA = System.nanoTime(),
			 timeB = timeA + DURACAO * 1000000000L;
		
		Graphics2D gScr = (Graphics2D) jogo.obterContextoGrafico();
		gScr.setColor(Color.BLACK);
		
		while(timeA < timeB)
		{
			if(img != null) img.paintIcon(this.jogo, gScr, 0, 0);
			else gScr.fillRect(0, 0, this.jogo.getLarguraDaTela(), this.jogo.getAlturaDaTela());
				
			this.mostrar();
						
			timeA = System.nanoTime();
			
			try {Thread.sleep(10);}
			catch (InterruptedException e){}
		}
		
		gScr.dispose();
	}
		
	private void mostrar()
	{
		if(!bufferStrategy.contentsLost()) bufferStrategy.show();
		else System.out.println("Contents Lost");
	}
	
	public void keyPressed(KeyEvent e)
	{
		if(e.getKeyCode() == KeyEvent.VK_ENTER)
			this.ativa = false;
	}
}