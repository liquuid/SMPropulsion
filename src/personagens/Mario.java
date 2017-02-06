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

package personagens;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.Timer;
import componentes.ObjetoAnimado;
import etc.GasPropulsor;
import etc.MarioAnimacao;
import jogo.SMPropulsion;

public class Mario extends ObjetoAnimado implements ActionListener
{	
	private static final long serialVersionUID = 1L;
	private static final int NUM_VIDAS = 3;
	private File somDePulo, somDeGases, somUuh, somDeath;
	private ImageIcon[] imgsDirSemItem, imgsEsqSemItem,
						imgsDirComCog, imgsEsqComCog;
	private SMPropulsion jogo;
	private int vidas = Mario.NUM_VIDAS, moedas = 0, gasAtual, numeroDeGases = 5, count = 0;
	private GasPropulsor tanqueDeGases[];
	private MarioAnimacao marioIntro;
	private boolean animar = false, soltandoGas = false;
	
	public Mario(SMPropulsion jogo)
	{
		super(jogo, new Point(0, 0), new Point(0, 0));
		this.jogo = jogo;
		
		this.vMaxAndando = 20;
		this.vMaxCorrendo = 30;
		this.gasAtual = this.numeroDeGases - 1;
		this.tanqueDeGases = new GasPropulsor[numeroDeGases];
		
		this.marioIntro = new MarioAnimacao(0, 0, 2, "mariodie", 100, this.jogo);
		this.jogo.animacao(this.marioIntro);
		
		for(int i = 0; i < numeroDeGases; ++i)
			this.tanqueDeGases[i] = new GasPropulsor(this.jogo);
		
		timer1 = new Timer(100, this);
		timer2 = new Timer(200, new Piscador());
		this.somDePulo = new File("sound/wav/jump.wav");
		this.somDeGases = new File("sound/wav/fart.wav");
		this.somUuh = new File("sound/wav/uuh.wav");
		this.somDeath = new File("sound/wav/death.wav");
		carregarImagens();

		this.setSize(new Dimension(imgsDirSemItem[0].getIconWidth(),
				   				   imgsDirSemItem[0].getIconHeight()));
		
		this.velMax = this.getvMaxAndando();
		this.moedas = 0;
	}
	
	private void carregarImagens()
	{
		String dirImgsMario = "img/mario/";
		
		imgsDirSemItem = new ImageIcon[7];
		for(int i = 0; i < 7; ++i)
			imgsDirSemItem[i] = new ImageIcon(dirImgsMario + "mario" + i + "d.png");

		imgsDirComCog = new ImageIcon[8];
		for(int i = 0; i < 8; ++i)
			imgsDirComCog[i] = new ImageIcon(dirImgsMario + "marioc" + i + "d.png");

		imgsEsqSemItem = new ImageIcon[7];
		for(int i = 0; i < 6; ++i)
			imgsEsqSemItem[i] = new ImageIcon(dirImgsMario + "mario" + i + "e.png");
		
		imgsEsqSemItem[6] = new ImageIcon(dirImgsMario + "mario6d.png");
		
		imgsEsqComCog = new ImageIcon[8];
		for(int i = 0; i < 7; ++i)
			imgsEsqComCog[i] = new ImageIcon(dirImgsMario + "marioc" + i + "e.png");
		
		imgsEsqComCog[7] = new ImageIcon(dirImgsMario + "marioc7d.png");
	}
	
	public void inicializar()
	{
		this.movendo = false;
		this.moverParaDireita = false;
		this.moverParaEsquerda = false;
		this.baixo = false;
		this.cima = false;
		this.impulsionar = false;
		this.correndo = false;
		this.saltando = false;
		this.noAr = false;
		this.viradoParaDireita = true;
		this.imortal = false;
		this.visivel = true;
		this.animar = false;
		this.ativo = true;
		this.mostrarImagem = true;
		this.controlavel = true;
		
		this.setLocation(60, 0);
		this.posicaoReal = new Point(60, 0);
		this.gasAtual = this.numeroDeGases - 1;
		this.imagemAtual = 0;
		this.moedas = 0;
		this.mudarEstado(ESTADO.SEM_ITEM);
	}
	
	public int gasAtual() { return this.gasAtual; }
	public void gasAtual(int g) { this.gasAtual = g; }
	
	public void atualizar()
	{
		if(this.controlavel) {
			atualizarImagens();
		
			if(this.ativo) {
				moverNaVertical();
				moverNaHorizontal();
			}
		
			alterarAceleracoes();
			verificarColisoes();
		}
	}
	
	public void moedas(int amount) { this.moedas += amount; }
	public int moedas() { return this.moedas; }
	
	public void vidas(int v) { this.vidas += v; }
	public int vidas() { return this.vidas; }
		
	private void alterarAceleracoes()
	{
		this.movendo = (moverParaEsquerda || moverParaDireita); //debug
		
		if(moverParaDireita && vDireita <= velMax) acelerarHorizontal(DIRECAO.DIREITA);
		else desacelerarHorizontal(DIRECAO.DIREITA);
		
		if(moverParaEsquerda && -vEsquerda <= velMax) acelerarHorizontal(DIRECAO.ESQUERDA);
		else desacelerarHorizontal(DIRECAO.ESQUERDA);
		
		if(impulsionar) acelerarVertical();
		else desacelerarVertical();
		
		sofrerGravidade();
	}
	
	private void acelerarHorizontal(DIRECAO dir)
	{
		switch(dir){
		case DIREITA:
			if(this.direcao == DIRECAO.DIREITA)
				if(vDireita < velMax) ++vDireita; break;
		case ESQUERDA:
			if(this.direcao == DIRECAO.ESQUERDA)
				if(vEsquerda > -velMax) --vEsquerda; break;
		}
	}
	
	private void desacelerarHorizontal(DIRECAO dir)
	{
		switch(dir){
		case DIREITA:
			if(vDireita > 0) --vDireita; break;
		case ESQUERDA:
			if(vEsquerda < 0) ++vEsquerda; break;
		}
	}
		
	private void acelerarVertical()
	{
		if(vVertical < VEL_MAX_SALTO)
			vVertical += 2;
	}
	
	private void desacelerarVertical()
	{
		if(vVertical > 0) --vVertical;
	}
	
	protected void moverNaVertical()
	{	
		if(this.y > jogo.getAlturaDaTela())
			this.morrer();
		
		//limiteInferior antes era == jogo.getAlturaDaTela() * 3/4
		int limiteInferior = this.jogo.getFaseAtual().limInfMario(),
			limiteSuperior = this.jogo.getFaseAtual().limSupMario();
		
		if(getMaxY() > limiteSuperior && (getMaxY() < limiteInferior || jogo.getFaseAtual().y() == 0))
		{ //livre na vertical
				super.moverNaVertical();
		}
		else if(getMaxY() <= limiteSuperior)
		{
			if(vVertical > 0)
			{ //subindo
				jogo.getFaseAtual().y(jogo.getFaseAtual().y() + (vVertical / 10));
				jogo.getFaseAtual().moverFundoNaVertical(vVertical, 10);
			}
			else
			{ //caindo
				super.moverNaVertical();
			}
		}
		else if(getMaxY() >= limiteInferior && jogo.getFaseAtual().y() != 0)
		{
			if(vVertical < 0) //caindo
				if(jogo.getFaseAtual().pos1F.y < jogo.getFaseAtual().getPlataforma(0).y - 605)
				{
					jogo.getFaseAtual().pos1F.y = jogo.getFaseAtual().getPlataforma(0).y - 605;
					jogo.getFaseAtual().pos2F.y = jogo.getFaseAtual().getPlataforma(0).y - 605;
					jogo.getFaseAtual().y(0);
				}
				else
				{
					jogo.getFaseAtual().y(jogo.getFaseAtual().y() + (vVertical / 20));
					jogo.getFaseAtual().moverFundoNaVertical(vVertical, 20);
				}
			else //caindo livre
			{
				super.moverNaVertical();
			}
		}
	}
	
	private void mudarEstado(ESTADO novoEstado)
	{
		switch(novoEstado)
		{
		case COM_COGUMELO:
			if(estado == ESTADO.SEM_ITEM)
				this.y -= (imgsDirComCog[0].getIconHeight() - imgsDirSemItem[0].getIconHeight());
			
			estado = novoEstado;
			setSize(imgsDirComCog[0].getIconWidth(), imgsDirComCog[0].getIconHeight());
			break;
		case SEM_ITEM:
			if(estado == ESTADO.COM_COGUMELO)
				this.y += (imgsDirComCog[0].getIconHeight() - imgsDirSemItem[0].getIconHeight());
			
			estado = novoEstado;
			setSize(imgsDirSemItem[0].getIconWidth(), imgsDirSemItem[0].getIconHeight());
			break;
		}
		
		imagemAtual = 0;
	}
	
	public void crescer() {
		this.count = 0;
		this.imortal = true;
		
		new Timer(100, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(count++ % 2 == 0) Mario.this.mudarEstado(ESTADO.COM_COGUMELO);
				else Mario.this.mudarEstado(ESTADO.SEM_ITEM);
	
				if(count == 8) {
					Mario.this.imortal = false;
					Mario.this.mudarEstado(ESTADO.COM_COGUMELO);
					((Timer) e.getSource()).stop();
				}
			}
		}).start();
	}
	
	public void abaixar()
	{
		if(!this.noAr && !this.movendo)
		{
			this.baixo = true;
			this.moverParaDireita = false;
			this.moverParaEsquerda = false;
			this.cima = false;
		
			int imagemAnterior = imagemAtual;
		
			switch(estado)
			{
			case SEM_ITEM:
				imagemAtual = 4;
				this.y += (imgsDirSemItem[imagemAnterior].getIconHeight() - imgsDirSemItem[imagemAtual].getIconHeight());
				setSize(imgsDirSemItem[imagemAtual].getIconWidth(), imgsDirSemItem[imagemAtual].getIconHeight());
				break;
			case COM_COGUMELO:
				imagemAtual = 5;
				this.y += (imgsDirComCog[imagemAnterior].getIconHeight() - imgsDirComCog[imagemAtual].getIconHeight());
				setSize(imgsDirComCog[imagemAtual].getIconWidth(), imgsDirComCog[imagemAtual].getIconHeight());
				break;
			}
		}
	}
	
	public void levantar()
	{
		if(!this.noAr)
		{
			this.baixo = false;
		
			int imagemAnterior = imagemAtual;
			imagemAtual = 0;
		
			switch(estado)
			{
			case SEM_ITEM:
				this.y -= (imgsDirSemItem[imagemAtual].getIconHeight() - imgsDirSemItem[imagemAnterior].getIconHeight());
				setSize(imgsDirSemItem[imagemAtual].getIconWidth(), imgsDirSemItem[imagemAtual].getIconHeight());
				break;
			case COM_COGUMELO:
				this.y -= (imgsDirComCog[imagemAtual].getIconHeight() - imgsDirComCog[imagemAnterior].getIconHeight());
				setSize(imgsDirComCog[imagemAtual].getIconWidth(), imgsDirComCog[imagemAtual].getIconHeight());
				break;
			}
		}
	}
	
	public void olharParaCima()
	{
		if(!this.noAr)
		{
			if(this.baixo)
			{
				this.baixo = false;
				this.levantar();
			}
		
			this.moverParaDireita = false;
			this.moverParaEsquerda = false;
			this.cima = true;
		
			switch(estado)
			{
				case SEM_ITEM:
					imagemAtual = 5;
					break;
				case COM_COGUMELO:
					imagemAtual = 6;
					break;
			}
		}
	}
		
	public void desenhar(Graphics g)
	{
		if(this.visivel) {
			if(viradoParaDireita)
				switch(estado) {
				case SEM_ITEM: imgsDirSemItem[imagemAtual].paintIcon(jogo, g, this.x, this.y); break;
				case COM_COGUMELO: imgsDirComCog[imagemAtual].paintIcon(jogo, g, this.x, this.y); break;
				}
			else
				switch(estado) {
				case SEM_ITEM: imgsEsqSemItem[imagemAtual].paintIcon(jogo, g, this.x, this.y); break;
				case COM_COGUMELO: imgsEsqComCog[imagemAtual].paintIcon(jogo, g, this.x, this.y); break;
				}
		
			for(GasPropulsor i : this.tanqueDeGases)
				if(i.ativo()) i.desenharGas(g);
		}
		if(SMPropulsion.DEBUG1_MOSTRAR_CONTORNO)
			this.jogo.desenharContorno(g, (int)this.getX(), (int)this.getY(), width, height);
	}
	
	private void atualizarImagens()
	{
		if((moverParaDireita || moverParaEsquerda) && !noAr)
		{ //animado
			if(correndo) //correndo
				timer1.setDelay(60);
			else //andando
				timer1.setDelay(100);
			
			if(!timer1.isRunning())
			{
				imagemAtual = 0;
				timer1.start();
			}
		}
		else //estático
		{
			if(timer1.isRunning())
				timer1.stop();
			
			if((!moverParaDireita && !moverParaEsquerda) && !noAr && !(baixo || cima) && controlavel)
				imagemAtual = 0; //parado
			else if(noAr)
				if(vVertical < 0)
					if(estado == ESTADO.SEM_ITEM) imagemAtual = 3; //caindo
					else imagemAtual = 4;
				else if(estado == ESTADO.SEM_ITEM) imagemAtual = 2; //pulando
					 else imagemAtual = 3;
		}
	}
	
	public void saltar()
	{
		if(this.baixo)
		{
			this.baixo = false;
			this.levantar();
		}
		
		if(!saltando && !noAr || SMPropulsion.DEBUG4_SALTO_NO_AR)
		{
			this.jogo.reproduzirEfeitoSonoro(this.somDePulo);
			
			inicioDoSaltoY = (int) this.getMaxY();
			impulsionar = true;
			saltando = true;
			vVertical = VEL_MAX_SALTO;
		}
	}
	
	private void verificarColisoes()
	{
		jogo.getFaseAtual().testarColisaoPara(this);
	}
	
	private void moverNaHorizontal()
	{
		this.moverNaHorizontal(this.vDireita);
		this.moverNaHorizontal(this.vEsquerda);
	}
		
	public void moverNaHorizontal(int vel)
	{
		//double velocidade = ((double)vel) / 10.0;
		int velocidade = vel / 10;

		posicaoReal.x += (velocidade);
	
		if(posicaoReal.x > jogo.getXCentral() && posicaoReal.x <= jogo.getFaseAtual().getLimiteDireito())
		{
			if(this.x != jogo.getXCentral())
				this.x = jogo.getXCentral();
			
			jogo.getFaseAtual().x(jogo.getFaseAtual().x() + (-velocidade));
			jogo.getFaseAtual().moverFundoNaHorizontal(-vel);
		}
		else //movimento livre
		{			
			this.x += (velocidade);
			corrigirPosicao();
		}
	}
		
	private void corrigirPosicao()
	{		
		if(posicaoReal.x < jogo.getXCentral())
		{
			jogo.getFaseAtual().x(0);
						
			if(this.x != posicaoReal.x)
				this.x = posicaoReal.x;
			
			if(this.x < jogo.getFaseAtual().getLimiteEsquerdo() - 80)
				posicaoReal.x = (this.x += 20);
		}
		else if(posicaoReal.x > jogo.getXCentral())
		{
			if(posicaoReal.x > jogo.getFaseAtual().getLimiteDireito() + jogo.getXCentral() + 80)
			{
				this.x -= 20;
				posicaoReal.x -= 20;
			}
		}
	}
	
	public void inverterDirecao()
	{
		moverParaDireita = false;
		moverParaEsquerda = false;
		
		vDireita ^= vEsquerda;
		vEsquerda ^= vDireita;
		vDireita ^= vEsquerda;
	}
		
	public void piscar()
	{
		if(!this.imortal) {
			mudarEstado(ESTADO.SEM_ITEM);
			imortal = true;
			if(!timer2.isRunning()) {
				this.jogo.reproduzirEfeitoSonoro(new File("sound/wav/powerdown.wav"));
				timer2.start();
			}
		}
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(timer1.isRunning())
			if(estado == ESTADO.SEM_ITEM)
				imagemAtual = (imagemAtual + 1) % 2;
			else
				imagemAtual = (imagemAtual + 1) % 3;
	}
	
	private class Piscador implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if(timer2.isRunning())
			{
				++piscou;
				visivel = !visivel;
				
				if(piscou > 10)
				{
					piscou = 0;
					visivel = true;
					imortal = false;
					timer2.stop();
				}
			}		
		}
	}
	
	public void soltarGases() {
		if(this.gasAtual >= 0) {
			this.soltandoGas(true);
			this.jogo.reproduzirEfeitoSonoro(this.somDeGases);
			this.tanqueDeGases[this.gasAtual].ativar();
			
			if(this.viradoParaDireita){
				if(this.noAr) this.vDireita = 50;
			}else{
				if(this.noAr) this.vEsquerda = -50;
			}
			
			--this.gasAtual;
		} else this.jogo.reproduzirEfeitoSonoro(this.somUuh);
	}
	
	public void testarColisoesCom(ObjetoAnimado sprite) {
		// TODO Auto-generated method stub
		
	}
	
	public void correr()
	{
		if(!this.noAr)
		{
			this.correndo = true;
			this.velMax = this.getvMaxCorrendo();
		}
	}
	
	public void andar(DIRECAO dir)
	{
		if(this.baixo)
		{
			this.baixo = false;
			this.levantar();
		}
		
		this.direcao = dir;
		this.moverParaDireita = this.viradoParaDireita = (dir == DIRECAO.DIREITA);
		this.moverParaEsquerda = (dir == DIRECAO.ESQUERDA);
		this.cima = false;
	}
	
	public int getvMaxAndando()
	{
		return this.vMaxAndando;
	}
	
	public int getvMaxCorrendo()
	{
		return this.vMaxCorrendo;
	}
	
	public void morrer() {
		if(this.ativo) {
			this.vEsquerda = 0;
			this.vDireita = 0;
			this.vVertical = 0;
			
			this.jogo.pararMusica();
			this.visivel = (this.controlavel = false);
		
			this.marioIntro.x(this.x);
			this.marioIntro.y(this.y);
			this.marioIntro.count(0);
			this.animar(true);

			this.jogo.reproduzirEfeitoSonoro(this.somDeath);
			this.ativo = false;
			
			--this.vidas;
		}
	}
	
	public boolean animar() { return this.animar; }
	public void animar(boolean a) { this.animar = a; }
	
	public void resetar() {
		this.vidas = Mario.NUM_VIDAS;
	}
	
	public boolean soltandoGas() { return this.soltandoGas; }
	public void soltandoGas(boolean sg) { this.soltandoGas = sg; }
	
	public void ativar() {}
	public void desativar() {}
	public void encerrar() {}
}
