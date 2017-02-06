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

package fases;

import java.awt.Graphics;
import java.awt.Point;
import java.io.File;
import java.util.Random;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.Timer;
import componentes.Blocos;
import componentes.CanoDeImpulso;
import componentes.Cubo;
import componentes.CuboDeSalto;
import componentes.CuboGravity;
import componentes.CuboSpeed;
import componentes.InterruptorFinalizador;
import componentes.ObjetoAnimado;
import componentes.ObjetoTangivel;
import componentes.ParDeInterruptores;
import componentes.Quadrado;
import componentes.itens.Moeda;
import etc.Gasometro;
import etc.ObjetoIntangivel;
import etc.Relogio;
import personagens.inimigos.Crocodilo;
import plataformas.Chao;
import plataformas.Nuvem;
import plataformas.Plataforma;
import jogo.SMPropulsion;

public class Fase1 extends Fase
{
	private int larguraF, alturaFC, ALTURA_DO_CHAO;
	private ImageIcon imgF, imgFC, imgFCM, imgFCFIM, intro, visorCoin, visorX, visorMario, gameOver;
	private Gasometro gasometro;
	
	public Fase1(SMPropulsion jogo)
	{
		this.tempoDeTurno = 14;

		this.jogo = jogo;
		
		this.imgF = new ImageIcon("img/cenarios/fase1f.png");
		this.imgFC = new ImageIcon("img/cenarios/fase1fc.png");
		this.imgFCM = new ImageIcon("img/cenarios/fase1fcm.png");
		this.imgFCFIM = new ImageIcon("img/cenarios/fase1fcFIM.png");
		this.intro = new ImageIcon("img/intro/fase1intro.png");
		this.gameOver = new ImageIcon("img/visor/gameover.png");
		
		this.visorCoin = new ImageIcon("img/objetos/coin1.png");
		this.visorX = new ImageIcon("img/visor/x.png");
		this.visorMario = new ImageIcon("img/visor/marioFace.png");
		
		this.relogio = new Relogio(jogo);
		this.gasometro = new Gasometro(jogo);
		
		this.larguraF = this.imgF.getIconWidth();
		this.alturaFC = this.imgFC.getIconHeight();
		
		this.timer1 = new Timer(2000, new Listener());
		this.musica = new File("sound/midi/dont.mid");
		
		this.instanciarComponentes();
		//this.inicializar();
	}
	
	public void instanciarComponentes()
	{	
		this.ALTURA_DO_CHAO = jogo.getAlturaDaTela() - 70;
		
		cenario = new ArrayList<Plataforma>();	
		this.adicionarCenario();
		
		objetos = new ArrayList<ObjetoAnimado>();
		if(!SMPropulsion.DEBUG2_GERAR_CHUVA_DE_INIMIGOS)
			this.adicionarObjetos();
		else {
			Random rand = new Random();
			int alcance, x1, yDesc,
				numeroDePersonagens = 100;

			for(int i = 0; i < numeroDePersonagens; ++i)
			{
				alcance = 50 + rand.nextInt(200);
				x1 = 50 + rand.nextInt(60) * 10;
				yDesc = 100 + rand.nextInt(100) * 10;
				objetos.add(new Crocodilo(alcance, jogo, new Point(x1, ALTURA_DO_CHAO - yDesc)));
			}
		}
	}
	
	private void adicionarCenario() {
		cenario.add(new Chao(jogo, new Point(-50, ALTURA_DO_CHAO), new Point(695, 800), "img/cenarios/plat1.png"));
		cenario.add(new Quadrado(jogo, new Point(900, ALTURA_DO_CHAO - 64), new Point(1350, 800), "img/cenarios/plat2.png"));
		cenario.add(new CanoDeImpulso(jogo, new Point(130, ALTURA_DO_CHAO - 32), new Point(162, ALTURA_DO_CHAO - 64), CanoDeImpulso.Tipo.LANCADOR_VERTICAL, 190, "img/objetos/canoJumpV_cim1.png"));
		cenario.add(new CuboDeSalto(jogo, new Point(80, ALTURA_DO_CHAO - 540), 100));
		cenario.add(new CanoDeImpulso(jogo, new Point(190, ALTURA_DO_CHAO - 669), new Point(222, ALTURA_DO_CHAO - 618), CanoDeImpulso.Tipo.LANCADOR_HORIZONTAL, 130, "img/objetos/canoJumpH_dir1.png"));
		cenario.add(new Nuvem(jogo, new Point(180, ALTURA_DO_CHAO - 622), 0, "nuvem.png", 260, Nuvem.TIPO.ESTATICA));
		cenario.add(new Nuvem(jogo, new Point(130, ALTURA_DO_CHAO - 622), 0, "nuvem.png", 260, Nuvem.TIPO.ESTATICA));
		
		cenario.add(new Cubo(jogo, new Point(530, ALTURA_DO_CHAO - 68)));
		
		cenario.add(new ObjetoIntangivel(jogo, new Point(222, ALTURA_DO_CHAO - 652), new Point(254, ALTURA_DO_CHAO - 616), null));
		cenario.add(new CanoDeImpulso(jogo, new Point(1100, ALTURA_DO_CHAO - 96), new Point(1132, ALTURA_DO_CHAO - 128), CanoDeImpulso.Tipo.LANCADOR_VERTICAL, 270, "img/objetos/canoJumpV_cim1.png"));
		cenario.add(new Nuvem(jogo, new Point(1080, ALTURA_DO_CHAO - 1200), 0, "nuvem.png", jogo.getAlturaDaTela() / 2, Nuvem.TIPO.ESTATICA));
		cenario.add(new CuboDeSalto(jogo, new Point(990, ALTURA_DO_CHAO - 1200), 80));
		cenario.add(new CuboDeSalto(jogo, new Point(900, ALTURA_DO_CHAO - 1200), 80));
		cenario.add(new CuboDeSalto(jogo, new Point(810, ALTURA_DO_CHAO - 1200), 80));
		cenario.add(new Nuvem(jogo, new Point(680, ALTURA_DO_CHAO - 1100), 300, "nuvem2.png", jogo.getAlturaDaTela() / 2, Nuvem.TIPO.MOVIMENTO_AUTOMATICO));
		cenario.add(new CuboSpeed(jogo, new Point(350, ALTURA_DO_CHAO - 1050), CuboSpeed.Tipo.DIREITA, 150));
		cenario.add(new CuboSpeed(jogo, new Point(700, ALTURA_DO_CHAO - 1020), CuboSpeed.Tipo.CIMA, 180));
		
		cenario.add(new CuboDeSalto(jogo, new Point(310, ALTURA_DO_CHAO - 1070), 80));
		cenario.add(new CuboDeSalto(jogo, new Point(270, ALTURA_DO_CHAO - 1110), 80));
		
		cenario.add(new Nuvem(jogo, new Point(170, ALTURA_DO_CHAO - 1120), 0, "nuvem.png", 280, Nuvem.TIPO.ESTATICA));
		cenario.add(new Nuvem(jogo, new Point(120, ALTURA_DO_CHAO - 1120), 0, "nuvem.png", 280, Nuvem.TIPO.ESTATICA));
		cenario.add(new CuboGravity(jogo, new Point(120, ALTURA_DO_CHAO - 1190), "cuboGravity", 2));
		cenario.add(new CuboGravity(jogo, new Point(360, ALTURA_DO_CHAO - 1490), "cuboGravity", 2));
		cenario.add(new Nuvem(jogo, new Point(360, ALTURA_DO_CHAO - 1220), 0, "nuvem.png", 280, Nuvem.TIPO.ESTATICA));
		cenario.add(new Nuvem(jogo, new Point(270, ALTURA_DO_CHAO - 1260), 0, "nuvem.png", 300, Nuvem.TIPO.ESTATICA));
		cenario.add(new CuboSpeed(jogo, new Point(283, ALTURA_DO_CHAO - 1620), CuboSpeed.Tipo.CIMA, 140));
		cenario.add(new CuboSpeed(jogo, new Point(283, ALTURA_DO_CHAO - 2000), CuboSpeed.Tipo.DIREITA, 140));
		cenario.add(new CuboSpeed(jogo, new Point(760, ALTURA_DO_CHAO - 2070), CuboSpeed.Tipo.BAIXO, -160));
		
		cenario.add(new Nuvem(jogo, new Point(900, ALTURA_DO_CHAO - 1900), 0, "nuvem.png", 300, Nuvem.TIPO.ESTATICA));
		cenario.add(new Nuvem(jogo, new Point(950, ALTURA_DO_CHAO - 1900), 0, "nuvem.png", 300, Nuvem.TIPO.ESTATICA));
		cenario.add(new CuboSpeed(jogo, new Point(1050, ALTURA_DO_CHAO - 1930), CuboSpeed.Tipo.CIMA, 100));
		
		cenario.add(new CuboGravity(jogo, new Point(900, ALTURA_DO_CHAO - 1970), "cuboGravity", 2));
		cenario.add(new Nuvem(jogo, new Point(800, ALTURA_DO_CHAO - 2100), 60, "nuvem3.png", 280, Nuvem.TIPO.DESLIZANTE));
		cenario.add(new CuboGravity(jogo, new Point(800, ALTURA_DO_CHAO - 2170), "cuboGravity", 2));

		cenario.add(new Blocos(jogo, new Point(-30, ALTURA_DO_CHAO - 2300), 280, Blocos.TIPO.HORIZONTAL));
		cenario.add(new Blocos(jogo, new Point(158, ALTURA_DO_CHAO - 2300), 280, Blocos.TIPO.HORIZONTAL));
		cenario.add(new Blocos(jogo, new Point(346, ALTURA_DO_CHAO - 2300), 280, Blocos.TIPO.HORIZONTAL));
		
		cenario.add(new CuboGravity(jogo, new Point(535, ALTURA_DO_CHAO - 2268), "cuboGravity", 2));
		cenario.add(new CuboSpeed(jogo, new Point(550, ALTURA_DO_CHAO - 2087), CuboSpeed.Tipo.ESQUERDA, -80));
		cenario.add(new CuboGravity(jogo, new Point(485, ALTURA_DO_CHAO - 2412), "cuboGravity", 2));
		
		cenario.add(new CuboSpeed(jogo, new Point(10, ALTURA_DO_CHAO - 2320), CuboSpeed.Tipo.DIREITA, 180));
		cenario.add(new CuboSpeed(jogo, new Point(1100, ALTURA_DO_CHAO - 2215), CuboSpeed.Tipo.CIMA, 120));
		cenario.add(new CuboSpeed(jogo, new Point(1100, ALTURA_DO_CHAO - 2180), CuboSpeed.Tipo.CIMA, 60));
		
		cenario.add(new CuboDeSalto(jogo, new Point(40, ALTURA_DO_CHAO - 2350), 100));
		
		cenario.add(new CuboGravity(jogo, new Point(1080, ALTURA_DO_CHAO - 2450), "cuboGravity", 2));
		
		cenario.add(new Nuvem(jogo, new Point(965, ALTURA_DO_CHAO - 2520), 0, "nuvem.png", 300, Nuvem.TIPO.ESTATICA));
		cenario.add(new Nuvem(jogo, new Point(1015, ALTURA_DO_CHAO - 2520), 0, "nuvem.png", 300, Nuvem.TIPO.ESTATICA));
		cenario.add(new Nuvem(jogo, new Point(1065, ALTURA_DO_CHAO - 2520), 0, "nuvem.png", 300, Nuvem.TIPO.ESTATICA));
		cenario.add(new CuboGravity(jogo, new Point(965, ALTURA_DO_CHAO - 2610), "cuboGravity", 2));
		
		cenario.add(new Quadrado(jogo, new Point(180, ALTURA_DO_CHAO - 500), new Point(276, ALTURA_DO_CHAO - 452), "img/objetos/musicbox.png"));
		cenario.add(new ParDeInterruptores(jogo, new Point(190, ALTURA_DO_CHAO - 532)));
		
		cenario.add(new InterruptorFinalizador(jogo, new Point(1073, ALTURA_DO_CHAO - 2550), "interruptor"));
	}
	
	private void adicionarObjetos() {
		objetos.add(new Crocodilo(480, jogo, new Point(200, ALTURA_DO_CHAO - 31)));
		objetos.add(new Crocodilo(350, jogo, new Point(150, ALTURA_DO_CHAO - 31)));
		
		objetos.add(new Moeda(jogo, new Point(140, ALTURA_DO_CHAO - 160)));
		objetos.add(new Moeda(jogo, new Point(140, ALTURA_DO_CHAO - 180)));
		
		objetos.add(new Moeda(jogo, new Point(280, ALTURA_DO_CHAO - 646)));
		objetos.add(new Moeda(jogo, new Point(300, ALTURA_DO_CHAO - 646)));
		
		for(int i = 0; i < 60; i += 20) objetos.add(new Moeda(jogo, new Point(930 + i, ALTURA_DO_CHAO - 1310))); //3
		for(int i = 0; i < 60; i += 20) objetos.add(new Moeda(jogo, new Point(830 + i, ALTURA_DO_CHAO - 1310))); //3
		
		objetos.add(new Moeda(jogo, new Point(540, ALTURA_DO_CHAO - 1050)));
		objetos.add(new Moeda(jogo, new Point(560, ALTURA_DO_CHAO - 1049)));
		objetos.add(new Moeda(jogo, new Point(580, ALTURA_DO_CHAO - 1048)));
		objetos.add(new Moeda(jogo, new Point(600, ALTURA_DO_CHAO - 1047)));
		objetos.add(new Moeda(jogo, new Point(700, ALTURA_DO_CHAO - 1560)));
		
		objetos.add(new Crocodilo(16, jogo, new Point(370, ALTURA_DO_CHAO - 1315)));
		
		for(int i = 0; i < 100; i += 20) objetos.add(new Moeda(jogo, new Point(285, ALTURA_DO_CHAO - (1480 + i)))); //5
		for(int i = 0; i < 200; i += 40) objetos.add(new Moeda(jogo, new Point(285, ALTURA_DO_CHAO - (1740 + i)))); //5
		for(int i = 0; i < 200; i += 40) objetos.add(new Moeda(jogo, new Point(752, ALTURA_DO_CHAO - (1740 + i)))); //5
		for(int i = 0; i < 40; i += 20) objetos.add(new Moeda(jogo, new Point(960 + i, ALTURA_DO_CHAO - 1930))); //2
		for(int i = 0; i < 100; i += 20) objetos.add(new Moeda(jogo, new Point(42, ALTURA_DO_CHAO - (2420 + i)))); //5
		for(int i = 0; i < 100; i += 20) objetos.add(new Moeda(jogo, new Point(700 + i, ALTURA_DO_CHAO - 2130))); //5
		
		objetos.add(new Moeda(jogo, new Point(551, ALTURA_DO_CHAO - 2150)));
			
		for(int i = 0; i < 40; i += 20) objetos.add(new Moeda(jogo, new Point(100 + i, ALTURA_DO_CHAO - 2320))); //2
		for(int i = 0; i < 80; i += 20) objetos.add(new Moeda(jogo, new Point(100 + i, ALTURA_DO_CHAO - 2250))); //2
		
		objetos.add(new Moeda(jogo, new Point(985, ALTURA_DO_CHAO - 2470)));
		
		//50 moedas
	}
	
	public void iniciarMusica() {
		this.jogo.reproduzirMusica(this.musica);
	}
	
	public void atualizar()
	{
		this.atualziarPersonagens();
		
		for(ObjetoAnimado i : this.objetos) //?
			i.testarAtivacao();
		
		for(ObjetoTangivel i : this.cenario)
			i.testarAtivacao();
	}
		
	private void atualziarPersonagens()
	{		
		for(ObjetoAnimado i: objetos)
			if(i.ativo) i.atualizar();
	}
		
	public void moverFundoNaHorizontal(int v)
	{
		int velocidade = v / 20;
		
		pos1F.x += velocidade;
		pos2F.x += velocidade;
		
		pos1FC1.x += velocidade;
		pos2FC1.x += velocidade;
		pos1FC2.x += velocidade;
		pos2FC2.x += velocidade;

		if(pos1F.x <= -larguraF) pos1FC2.x = pos1FC1.x = pos1F.x = 0;
		if(pos1F.x >= larguraF) pos1FC2.x = pos1FC1.x = pos1F.x = 0;
		if(pos2F.x <= 0) pos2FC2.x = pos2FC1.x = pos2F.x = (pos1F.x + larguraF);
		if(pos2F.x >= larguraF) pos2FC2.x = pos2FC1.x = pos2F.x = (pos1F.x - larguraF);
	}
	
	public void moverFundoNaVertical(int v, int escala)
	{
		pos1F.y += (v / escala);
		pos2F.y += (v / escala);
		
		pos1FC1.y += (v / escala);
		pos2FC1.y += (v / escala);
		pos1FC2.y += (v / escala);
		pos2FC2.y += (v / escala);
		
		if(pos1FC1.y >= alturaFC) pos2FC1.y = pos1FC1.y = (pos1FC2.y - alturaFC);//s
		if(pos1FC1.y <= -alturaFC) pos2FC1.y = pos1FC1.y = 0;
		if(pos1FC2.y <= 0) pos2FC2.y = pos1FC2.y = (pos1FC1.y + alturaFC);
		if(pos1FC2.y >= alturaFC) pos2FC2.y = pos1FC2.y = (pos1FC1.y - alturaFC);//s
	}
	
	public void inicializarPosicoes()
	{
		this.x = 0;
		this.y = 0;
		
		pos1F = new Point(0, this.ALTURA_DO_CHAO - 605);
		pos2F = new Point(larguraF, pos1F.y);
			
		pos1FC1 = new Point(0, 0);
		pos2FC1 = new Point(larguraF, pos1FC1.y);
		pos1FC2 = new Point(0, pos1FC1.y - alturaFC);
		pos2FC2 = new Point(larguraF, pos1FC2.y);
	}
	
	public void desenharFundo(Graphics g)
	{ //objetos desenhados antes do mario
		imgFC.paintIcon(jogo, g, pos1FC1.x, pos1FC1.y);
		imgFC.paintIcon(jogo, g, pos2FC1.x, pos2FC1.y);
		imgFC.paintIcon(jogo, g, pos1FC2.x, pos1FC2.y);
		imgFC.paintIcon(jogo, g, pos2FC2.x, pos2FC2.y);
		
		//g.setColor(Color.RED);
		//g.fillRect(pos1FC1.x, pos1FC1.y, this.alturaFC, this.imgFC.getIconHeight());
		//g.setColor(Color.GREEN);
		//g.fillRect(pos1FC2.x, pos1FC2.y, this.alturaFC, this.imgFC.getIconHeight());
		
		imgFCM.paintIcon(jogo, g, pos1F.x, pos1F.y - 620);
		imgFCM.paintIcon(jogo, g, pos2F.x, pos1F.y - 620);
		
		imgF.paintIcon(jogo, g, pos1F.x, pos1F.y);
		imgF.paintIcon(jogo, g, pos2F.x, pos2F.y);
		
		imgFCFIM.paintIcon(jogo, g, pos1FC1.x, pos1F.y - 2600);
		imgFCFIM.paintIcon(jogo, g, pos2FC1.x, pos1F.y - 2600);
		
		if(pos1F.y - 2600 > -200) this.jogo.getMario().morrer();
		
		//g.setColor(Color.RED);
		//g.fillRect(pos2F.x, pos2F.y, this.larguraF, this.imgF.getIconHeight());
		//g.setColor(Color.GREEN);
		//g.fillRect(pos1F.x, pos1F.y, this.larguraF, this.imgF.getIconHeight());
	}
	
	public void desenharComponentes(Graphics g)
	{ //objetos desenhados depois do mario
		for(ObjetoTangivel i: cenario)
			if(i.ativo) i.desenhar(g);
		
		for(ObjetoAnimado i: objetos)
			if(i.ativo) i.desenhar(g);
		
		this.desenharVisor(g);
		
		if(mostrarIntro) intro.paintIcon(jogo, g, 185, 190);
		if(mostrarGameOver) gameOver.paintIcon(jogo, g, 140, 190);
	}
	
	private void desenharVisor(Graphics g) {
		
		this.visorMario.paintIcon(this.jogo, g, 10, 75);
		this.visorX.paintIcon(this.jogo, g, 25, 75);
		this.jogo.digito(this.jogo.getMario().vidas()).paintIcon(this.jogo, g, 40, 75);
		
		this.visorCoin.paintIcon(this.jogo, g, 75, 75);
		this.jogo.digito(this.jogo.getMario().moedas() / 10).paintIcon(this.jogo, g, 87, 76);
		this.jogo.digito(this.jogo.getMario().moedas() % 10).paintIcon(this.jogo, g, 97, 76);
		
		this.relogio.desenhar(g);
		this.gasometro.desenhar(g);
	}
		
	public int getLimiteEsquerdo()
	{
		return (int) cenario.get(0).x + 90;
	}
	
	public int getLimiteDireito()
	{
		return (int) cenario.get(1).x + (int) cenario.get(1).width - jogo.getXCentral() - this.comprimento();
		//770
	}
}