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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.io.File;

import javax.swing.ImageIcon;

import etc.Animacao;
import etc.MarioAnimacao;
import etc.Temporizador;

import jogo.SMPropulsion;

public class MenuInicial extends KeyAdapter implements Animacao
{
	private enum OPCAO {
		INICIAR,
		RECORDS,
		COMANDOS;
	}
	
	protected final String IMAGE_DIR = "img/intro/";
	protected SMPropulsion jogo;
	protected BufferStrategy bufferStrategy;
	protected boolean mostrarMenu;
	protected ImageIcon img1, logo, iniciar, records, comandos;
	protected int posYimg1, posYimg2, posYicon, vel, posXini, posXrec, posYcom;
	protected MarioAnimacao mario;
	protected MenuRecords menuRecords;
	protected MenuComandos menuComandos;
	protected OPCAO opcao;
	protected File som1, som2;
	
	public MenuInicial(SMPropulsion jogo, BufferStrategy bs)
	{		
		this.jogo = jogo;
		this.bufferStrategy = bs;
		
		this.mario = new MarioAnimacao(190, 355, 150);
		this.mario.iniciarAnimacao();
		this.menuRecords = new MenuRecords(this, jogo);
		this.menuComandos = new MenuComandos(this, jogo);
		
		this.img1 = new ImageIcon(IMAGE_DIR + "intro4.gif");
		this.logo = new ImageIcon(IMAGE_DIR + "logo.png");
		this.iniciar = new ImageIcon(IMAGE_DIR + "iniciar.png");
		this.records = new ImageIcon(IMAGE_DIR + "records.png");
		this.comandos = new ImageIcon(IMAGE_DIR + "comandos.png");
		this.posYimg1 = 0;
		this.posYimg2 = -img1.getIconHeight();
		this.posYcom = 300;
		this.posYicon = 130;
		this.posXini = 210;
		this.posXrec = 208;
		this.vel = 2;
		this.som1 = new File("sound/wav/fireball.wav");
		this.som2 = new File("sound/wav/kick.wav");
		
		this.mario.y(255);
		this.opcao = OPCAO.INICIAR;
		this.inicializar();
	}
	
	public void inicializar()
	{
		this.jogo.removeKeyListener(this);
		this.jogo.addKeyListener(this);
		this.mostrarMenu = true;
	}
	
	public void exibir()
	{	
		this.jogo.reproduzirMusica(new File("sound/midi/intro.mid"));
		this.loop();
	}
		
	private void mostrar()
	{
		if(!bufferStrategy.contentsLost()) bufferStrategy.show();
		else System.out.println("Contents Lost");
	}
	
	public void keyPressed(KeyEvent e)
	{
		if(e.getKeyCode() == KeyEvent.VK_ENTER)
			this.irParaOpcao();
		else if(e.getKeyCode() == KeyEvent.VK_UP)
			this.mudarOpcao(1);
		else if(e.getKeyCode() == KeyEvent.VK_DOWN)
			this.mudarOpcao(-1);
	}
	
	private void mudarOpcao(int i)
	{
		int addToNextOp = 20;
		OPCAO opTemp = this.opcao;
		
		switch(this.opcao){
		case INICIAR:
		this.opcao = (i == 1 ? OPCAO.INICIAR : OPCAO.RECORDS);
		break;
		case RECORDS:
			this.opcao = (i == 1 ? OPCAO.INICIAR : OPCAO.COMANDOS);
		break;
		case COMANDOS:
			this.opcao = (i == 1 ? OPCAO.RECORDS : OPCAO.COMANDOS);
		break;
		}
		
		if(opTemp != this.opcao){
			this.mario.y(this.mario.y() + -i * addToNextOp);
			this.jogo.reproduzirEfeitoSonoro(this.som1);
		}
	}

	public void loop() {		
		Graphics2D gScr = (Graphics2D) jogo.obterContextoGrafico();
		Temporizador temp = new Temporizador(this, 8);
		
		while(this.mostrarMenu) {
			this.atualizar();
			this.desenhar(gScr);
			this.mostrar();
			temp.normalizar();
			temp.pularQuadros();
		}
		this.esconderIcones();
		gScr.dispose();
	}
	
	private void irParaOpcao()
	{
		switch(this.opcao){
		case INICIAR:
			this.jogo.removeKeyListener(this);
			this.mostrarMenu = false;
		break;
		case RECORDS:
			this.jogo.reproduzirEfeitoSonoro(this.som2);
			this.jogo.removeKeyListener(this);
			this.jogo.addKeyListener(this.menuRecords);
			this.menuRecords.mostrar = true;
		break;
		case COMANDOS:
			this.jogo.reproduzirEfeitoSonoro(this.som2);
			this.jogo.removeKeyListener(this);
			this.jogo.addKeyListener(this.menuComandos);
			this.menuComandos.mostrar = true;
		break;
		}
	}
	
	public void atualizar() {
		posYimg1 += vel;
		posYimg2 += vel;
				
		if(posYimg1 > img1.getIconHeight()) posYimg1 = 0;
		if(posYimg2 > 0) posYimg2 = posYimg1 - img1.getIconHeight();
	}

	public void desenhar(Graphics2D gScr) {
		gScr.setColor(Color.BLACK);
		gScr.fillRect(0, 0, this.jogo.getLarguraDaTela(), this.jogo.getAlturaDaTela());
				
		img1.paintIcon(this.jogo, gScr, 0, posYimg1);
		img1.paintIcon(this.jogo, gScr, 0, posYimg2);
		logo.paintIcon(this.jogo, gScr, 40, posYicon);
		iniciar.paintIcon(this.jogo, gScr, this.posXini, 260);
		records.paintIcon(this.jogo, gScr, this.posXrec - 1, 280);
		comandos.paintIcon(this.jogo, gScr, 211, this.posYcom);
		mario.desenhar(gScr);
		
		if(this.menuRecords.mostrar) this.menuRecords.desenhar(gScr);
		if(this.menuComandos.mostrar) this.menuComandos.desenhar(gScr);
		
		this.jogo.desenharTarjas(gScr);
	}
	
	private void esconderIcones() {
		Graphics2D gScr = (Graphics2D) jogo.obterContextoGrafico();
		Temporizador temp = new Temporizador(this, 8);
		
		while(this.posXrec < this.jogo.getLarguraDaTela()){
			this.atualizar2();
			this.desenhar(gScr);
			this.mostrar();
			temp.normalizar();
			temp.pularQuadros();
		}
		
		gScr.dispose();
	}
	
	private void atualizar2()
	{
		this.atualizar();
		
		this.posXini -= 6;
		this.posXrec += 6;
		this.posYicon -= 6;
		this.posYcom += 6;
		this.mario.y(this.mario.y() + 4);
	}
}
