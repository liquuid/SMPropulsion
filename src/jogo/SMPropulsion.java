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

package jogo;

import intro.DadosRecords;
import intro.Introducao;
import intro.MenuInicial;

import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import personagens.Mario;
import etc.Audio;
import etc.Animacao;
import etc.OuvinteDeTeclado;
import etc.Temporizador;
import fases.Fase;
import fases.Fase1;

public class SMPropulsion extends JFrame implements Animacao
{
	public static boolean DEBUG1_MOSTRAR_CONTORNO = false,
						  DEBUG2_GERAR_CHUVA_DE_INIMIGOS = false,
						  DEBUG3_TIRAR_ABERTURA = false,
						  DEBUG4_SALTO_NO_AR = false;
	
	private static boolean TELA_CHEIA = false;
	
	private static final long serialVersionUID = 1L;
	private int xCentral;
	
	private Temporizador temporizador;
	private Audio audio;
	private Mario mario;
	private Fase faseAtual;
	private boolean pausado = false, ativo = false;
	private File somDePause;
	private DadosRecords records;
	private ImageIcon digitos[];
	private Animacao animacao = null;
	private OuvinteDeTeclado ouvinte;
	
	private GraphicsConfiguration gc;
	private GraphicsDevice gd;
	private BufferStrategy bufferStrategy;
	  
	private int larguraDaTela, alturaDaTela;
				
	public SMPropulsion()
	{
		super("Super Mario Propulsion");
		
		this.ativo = true;
		
		this.records = new DadosRecords();
		this.lerRecords();
		
		iniciarTelaCheia();
		
		xCentral = (larguraDaTela / 2) - 20;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.audio = new Audio();
		this.somDePause = new File("sound/wav/pause.wav");
		this.ouvinte = new OuvinteDeTeclado(this);
		
		this.faseAtual = new Fase1(this);
		
		this.digitos = new ImageIcon[10];
		for(int i = 0; i < this.digitos.length; ++i)
			this.digitos[i] = new ImageIcon("img/visor/" + i + ".png");
		
		this.mario = new Mario(this);
		this.temporizador = new Temporizador(this, this.getFaseAtual().tempoDeTurno);
	}
	
	public boolean ativo() { return this.ativo; }
	
	public void desenharContorno(Graphics g, int x, int y, int width, int height) {
		g.setColor(Color.YELLOW);
		g.drawRect(x, y, width, height);
	}
			
	public void iniciarJogo()
	{
		if(!SMPropulsion.DEBUG3_TIRAR_ABERTURA) {
			new Introducao(this, bufferStrategy).exibir();
			    
			MenuInicial menu = new MenuInicial(this, bufferStrategy);
			menu.exibir();
			menu.loop();
		}
		
	    System.gc();
		this.adicionarTratadorDeTeclado();
		
		this.faseAtual.inicializar();
		this.faseAtual.ativa(true);
		this.getMario().resetar();
		this.getMario().inicializar();
	}
		
	public void loop()
	{
		this.reiniciarTempo();
		
		while(this.getFaseAtual().ativa()) //loop principal
		{
			if(!this.pausado)
			{ //inicio do turno
				this.atualizar();
				this.renderizar(); //desenha no buffer e renderizar
			} //fim do turno

			this.temporizador.normalizar();
			this.temporizador.pularQuadros();
			this.temporizador.gerarEstatisticas();
		} //fim do loop principal
		
		this.removeKeyListener(this.ouvinte);
		this.pararMusica();
	}
		
	public void atualizar()
	{		
		this.getMario().atualizar();
		this.getFaseAtual().atualizar();
		if(this.getMario().animar()) this.animacao.atualizar();
	}
	
	public void desenhar(Graphics2D gScr)
	{	
		gScr.setColor(Color.WHITE);
		gScr.fillRect(0, 0, larguraDaTela, alturaDaTela); //limpar fundo
		
		this.getFaseAtual().desenharFundo(gScr);
		this.getMario().desenhar(gScr);
		this.getFaseAtual().desenharComponentes(gScr);
		if(this.getMario().animar()) this.animacao.desenhar(gScr);
		
		desenharTarjas(gScr); //tarjas pretas
		
		gScr.setColor(Color.DARK_GRAY);
		//gScr.drawString(String.format("posicao2Fundo.x == %d", this.getFaseAtual().pos2F.x), 5, 40);//
		//gScr.drawString(String.format("posicao2Fundo.y == %d", this.getFaseAtual().pos2F.y), 5, 50);//
		//gScr.drawString(String.format("mario.soltandoGas == %b", this.getMario().soltandoGas()), 5, 60);//
		//gScr.drawString(String.format("mario.vDireita == %d", this.getMario().vDireita), 5, 70);//
		gScr.drawString(String.format("FPS: %.2f   UPS: %.2f", this.temporizador.getFps(), this.temporizador.getUps()), 352, 390);//
		//gScr.drawString(String.format("totalDeQuadrosPulados: %d", this.temporizador.quadrosPulados()), 470, 60);//
		//gScr.drawString(String.format("tempoDeJogo: %d segs", this.getTempoDecorrido()), 470, 70);//
	}
	
	private void renderizar() //usando active-rendering
	{
		try
		{
			Graphics2D gScr = (Graphics2D) this.obterContextoGrafico();
			desenhar(gScr); //desenhar no buffer
			gScr.dispose();
			
			if(!bufferStrategy.contentsLost()) bufferStrategy.show();
			else System.out.println("Contents Lost");

			Toolkit.getDefaultToolkit().sync();
	    }
		catch(Exception e){e.printStackTrace();}
	}
	
	public int getTempoDecorrido() { //retorna tempo em segundos
		return (int) (this.temporizador.tempoDecorrido() / 1000000000L);
	}
	
	public ImageIcon digito(int i) {
		
		if(i > 9) return this.digitos[0];
		else return this.digitos[i];
	}
	
	public void reiniciarTempo() {
		this.temporizador.reiniciar();
	}
	
	public void desenharTarjas(Graphics gScr)
	{
		final int ALTURA_DA_TARJA = 50;
		final int ALTURA_DA_BARRA_DO_FRAME = 20;
		
		gScr.setColor(Color.BLACK);
		gScr.fillRect(0, 0, this.larguraDaTela, ALTURA_DA_TARJA + ALTURA_DA_BARRA_DO_FRAME);
		gScr.fillRect(0, alturaDaTela - ALTURA_DA_TARJA, this.larguraDaTela, ALTURA_DA_TARJA);
	}
	
	public Graphics obterContextoGrafico()
	{
		return this.bufferStrategy.getDrawGraphics();
	}
						
	private void iniciarTelaCheia()
	{
		int width = 512,
			height = 400,
			bitDepth = 16;
		
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		gd = ge.getDefaultScreenDevice();
		gc = ge.getDefaultScreenDevice().getDefaultConfiguration();
		
		//setUndecorated(true);
		setIgnoreRepaint(true);
		setResizable(false);
		
		if(!(gd.isFullScreenSupported() && TELA_CHEIA))
		{
			System.out.println("FSEM nao suportado");
			iniciarEmModoTeste(width, height, bitDepth);
		}
		else
		{				
			if(displayModeDisponivel(width, height, bitDepth))
			{
				int refreshRate = getRefreshRatePara(width, height, bitDepth);
				
				if(refreshRate != 0)
				{
					gd.setFullScreenWindow(this);
					this.setDisplayMode(width, height, bitDepth, refreshRate);
				}
				else
					iniciarEmModoTeste(width, height, bitDepth);
			}
			else
				iniciarEmModoTeste(width, height, bitDepth);
		}
		
		larguraDaTela = getBounds().width;
		alturaDaTela = getBounds().height;
		
		setBufferStrategy();
	}
	
	private void iniciarEmModoTeste(int width, int height, int bitDepth)
	{
		gd = null;
		
		setSize(width, height);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private void setDisplayMode(int width, int height, int bitDepth, int refreshRate)
	{
		if(!gd.isDisplayChangeSupported())
		{
			System.out.println("Alteracao de display nao suportada");
			iniciarEmModoTeste(width, height, bitDepth);
			return;
		}
		
		DisplayMode dm = new DisplayMode(width, height, bitDepth, refreshRate);
		
		try
		{
			gd.setDisplayMode(dm);
			System.out.println("Display mode alterado para: (" + width + "," +
	                            height + "," + bitDepth + ")");
		}
		catch(IllegalArgumentException e) 
		{
			System.out.println("Erro ao alterar Display mode (" + width + "," +
	                            height + "," + bitDepth + ")");
		}

		try
		{
			Thread.sleep(1000);
		}
		catch(InterruptedException ex){}
	}
	
	private int getRefreshRatePara(int width, int height, int bitDepth)
	{
		int refRatePadrao = 75,
			refRateAceitavel = 85;
		
		boolean padrao = false,
				aceitavel = false;
		
		DisplayMode[] modes = gd.getDisplayModes();
		
		for(DisplayMode mode: modes)
		{
			if(mode.getWidth() == width &&
			   mode.getHeight() == height &&
			   mode.getBitDepth() == bitDepth)
				if(mode.getRefreshRate() == refRatePadrao)
					padrao = true;
				else if(mode.getRefreshRate() == refRateAceitavel)
					aceitavel = true;
		}
		
		if((padrao && aceitavel) || aceitavel)
			return refRateAceitavel;
		else if(padrao)
			return refRatePadrao;
		else return 0;
	}
	
	private boolean displayModeDisponivel(int width, int height, int bitDepth)
	{
		DisplayMode[] modes = gd.getDisplayModes();
		
	    for(int i = 0; i < modes.length; i++)
	    {
	    	if(width == modes[i].getWidth() && height == modes[i].getHeight() &&
	    	   bitDepth == modes[i].getBitDepth())
	    		return true;
	    }
	    return false;
	}
	
/*	private void showModes(DisplayMode[] modes)
	{
		System.out.println("Modes");
	    
		for(int i = 0; i < modes.length; i++)
		{
			System.out.print("(" + modes[i].getWidth() + "," +
	                       	 modes[i].getHeight() + "," +
	                       	 modes[i].getBitDepth() + "," +
	                       	 modes[i].getRefreshRate() + ")  " );
	      
			if((i+1)%4 == 0)
				System.out.println();
		}
		System.out.println();
	}*/
	  
	private void setBufferStrategy()
	{
		try
		{
			EventQueue.invokeAndWait(
				new Runnable()
				{
					public void run() 
					{
						createBufferStrategy(2); //Page Flipping
					}
				}
			);
	    }
	    catch (Exception e)
	    {  
	    	System.out.println("Error ao criar buffer strategy");  
	    	System.exit(0);
	    }

	   //try{Thread.sleep(10);}
	   //catch(InterruptedException ex){}

	    bufferStrategy = getBufferStrategy();
	}
	
	public BufferStrategy getBS(){ return this.bufferStrategy; }
	  	
	public int getXCentral()
	{
		return this.xCentral;
	}
		
	public Fase getFaseAtual()
	{
		return this.faseAtual;
	}
	
	public Mario getMario()
	{
		return this.mario;
	}
	
	public boolean estaPausado()
	{
		return this.pausado;
	}
	
	public void setPausado(boolean pausado)
	{
		this.pausado = pausado;
	}
	
	public void pausar()
	{
		setPausado(!estaPausado());
		
		if(estaPausado())
			this.reproduzirEfeitoSonoro(this.somDePause);
	}
	
	public void reproduzirEfeitoSonoro(File audio)
	{
		this.audio.tocar(audio);
	}
	
	public void reproduzirMusica(File audio)
	{
		if(!this.audio.estaTocando())
			this.audio.tocarMusica(audio);
	}
	
	public void pararMusica()
	{
		if(this.audio.estaTocando())
			this.audio.pararMusica();
	}
	
	public boolean estaReproduzindoSom() {
		return this.audio.estaReproduzindo();
	}
	
	public boolean estaTocandoMusica() {
		return this.audio.estaTocando();
	}
		
	public void adicionarTratadorDeTeclado()
	{
		this.removeKeyListener(this.ouvinte);
		this.addKeyListener(this.ouvinte);
	}
	
	public GraphicsConfiguration getGc()
	{
		return this.gc;
	}

	public int getAlturaDaTela()
	{
		return alturaDaTela;
	}
	
	public int getLarguraDaTela()
	{
		return larguraDaTela;
	}
	
	public void animacao(Animacao a) { this.animacao = a; }
	public Animacao animacao() { return this.animacao; }
	
	public void encerrarAplicacao()
	{
		this.getFaseAtual().ativa(false);
		this.ativo = false;
		
		if(gd != null)
		{
			Window w = gd.getFullScreenWindow();
		
			if(w != null) w.dispose();
			gd.setFullScreenWindow(null);
		}
	}
	
	public void terminar() {
		new Thread(){
			public void run() {
				SMPropulsion.this.getMario().controlavel = false;

				SMPropulsion.this.pararMusica();
				SMPropulsion.this.reproduzirEfeitoSonoro(new File("sound/wav/finish.wav"));
				
				int points = (999 - SMPropulsion.this.getTempoDecorrido()) * SMPropulsion.this.getMario().moedas() + (SMPropulsion.this.getMario().moedas() == 50 ? 10000 : 0);
				
				JOptionPane.showMessageDialog(SMPropulsion.this, "Você obteve " + points + " pontos.", "Super Mario Propulsion", JOptionPane.PLAIN_MESSAGE);
				
				String name = null;
				if(SMPropulsion.this.records.novoRecord(points)) {
					SMPropulsion.this.reproduzirEfeitoSonoro(new File("sound/wav/wahoo.wav"));
					name = JOptionPane.showInputDialog(SMPropulsion.this, "Por favor, digite seu nome: ", "Novo record!", JOptionPane.INFORMATION_MESSAGE);
				
					if(name != null) {
						if(name.compareTo("") == 0 || name.charAt(0) == ' ') name = "anônimo";
						name = name.substring(0, name.length() > 8 ? 7 : name.length());
						SMPropulsion.this.records.inserirNovoDado(name, String.format("%d", points));
						SMPropulsion.this.salvarRecords();
						JOptionPane.showMessageDialog(SMPropulsion.this, "Record salvo!\nSeu nome se encontra na lista visível pelo menu \"VER RECORDS\".", "Super Mario Propulsion", JOptionPane.PLAIN_MESSAGE);
					}
				}
				
				SMPropulsion.this.getFaseAtual().ativa(false);
			}
		}.start();
	}
	
	private void lerRecords()
	{
		try
		{
			ObjectInputStream input = new ObjectInputStream(new FileInputStream("records.dat"));
			this.records = (DadosRecords) input.readObject();
			input.close();
		}
		catch (FileNotFoundException e)
		{		
			this.records.criarNovaLista();
			salvarRecords();
			System.out.println("Novo arquivo records.dar criado!");
			//System.exit(1);
		}
		catch (IOException e)
		{			
			imprimirMensagemDeErro("IOException");
			System.exit(1);
		}
		catch (ClassNotFoundException e)
		{
			imprimirMensagemDeErro("ClassNotFoundException");
			System.exit(1);
		}
	}
	
	private void salvarRecords()
	{
		try
		{
			ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("records.dat"));
			output.writeObject(this.records);
			output.close();
		}
		catch(FileNotFoundException e)
		{
			imprimirMensagemDeErro("FileNotFoundException");
		}
		catch(IOException e)
		{
			imprimirMensagemDeErro("IOException");
		}
	}
	
	private void imprimirMensagemDeErro(String message)
	{
		JOptionPane.showMessageDialog(this,
		"Ocorreu um problema ao salvar os dados.\n",
		message, JOptionPane.INFORMATION_MESSAGE);
	}
	
	public DadosRecords records() {
		return this.records;
	}
	
	public boolean pausado() {
		return this.pausado;
	}
		
	public static void main(String args[])
	{
		SMPropulsion smp = new SMPropulsion();
		
		while(smp.ativo) {
			smp.iniciarJogo();
			smp.loop();
			
			System.gc();
		}
		
		System.exit(0);
	}
}
