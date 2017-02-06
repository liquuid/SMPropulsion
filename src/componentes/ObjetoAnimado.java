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

package componentes;

import java.awt.Point;
import java.io.File;

import javax.swing.Timer;


import jogo.SMPropulsion;

public abstract class ObjetoAnimado extends ObjetoTangivel
{
	public enum DIRECAO
	{
		DIREITA,
		ESQUERDA,
		CIMA,
		BAIXO;
	}
	
	public enum ESTADO
	{
		SEM_ITEM,
		COM_COGUMELO;
	}
	
	private static final long serialVersionUID = 1L;
	
	public int piscou = 0, velMax = 20, vVertical = 0, /*vHorizontal = 0,*/ vDireita = 0,
	   		   vEsquerda = 0, inicioDoSaltoY = 0, imagemAtual = 0;
	protected File kick;
	protected Point posicaoReal;
	
	public boolean movendo = false,
				   moverParaDireita = true,
				   moverParaEsquerda = false,
				   controlavel = false,
				   baixo = false,
				   cima = false,
				   impulsionar = false,
		   		   correndo = false,
		   		   saltando = false,
		   		   noAr = false,
		   		   viradoParaDireita = true,
		   		   mostrarImagem = false,
		   		   imortal = false,
		   		   visivel = true;
	
	protected int vMaxAndando, vMaxCorrendo;
	protected DIRECAO direcao = DIRECAO.DIREITA;
	protected ESTADO estado = ESTADO.SEM_ITEM;
	protected Timer timer1, timer2;
	protected final int VEL_MAX_SALTO = 40;
	
	public ObjetoAnimado(SMPropulsion jogo, Point p1, Point p2)
	{
		super(jogo, p1, p2);
		
		this.kick = new File("sound/wav/kick.wav");
	}
	
	public void encerrarVoo() {
		
	}
	
	public void abaixar()
	{

	}
	
	public void levantar()
	{

	}
	
	protected void sofrerGravidade()
	{
		if(this.jogo.getFaseAtual().gravidade()) this.gravidadeA();
		else this.gravidadeB();
	}
	
	private void gravidadeB() {
		if(vVertical < 20) vVertical += -this.jogo.getFaseAtual().forcaDaGravidade();	
	}

	private void gravidadeA() {
		if(jogo.getFaseAtual().pisouEmPlataforma(this))
		{
			noAr = false;
			saltando = false;
			vVertical = 0;
		}
		else
		{
			noAr = true;
			
			if(vVertical > -(VEL_MAX_SALTO + 20)) vVertical += this.jogo.getFaseAtual().forcaDaGravidade();
			else vVertical = -(VEL_MAX_SALTO + 20);
		}
				
		if(Math.abs(inicioDoSaltoY - this.getMaxY()) >= 40)
			impulsionar = false;
	}
	
	protected void moverPara(DIRECAO direcao)
	{
		switch(direcao)
		{
		case ESQUERDA:
			this.x += (-vEsquerda / 10); break;
		case DIREITA:
			this.x += (vDireita / 10); break;
		case CIMA:
		case BAIXO:
			this.y -= (vVertical / 10); break;//cima(+) baixo(-)
		}
			
	}
	public Point posicaoReal() { return this.posicaoReal; }
	
	public DIRECAO getDirecao()
	{
		return this.direcao;
	}
	
	/*protected void moverParaDireita()
	{
		this.x += (vDireita / 10);
	}
	
	protected void moverParaEsquerda()
	{
		this.x -= (vEsquerda / 10);
	}*/
		
	protected void moverNaVertical()
	{
		this.y -= (vVertical / 10); //cima(+) baixo(-)
	}
	
	public void kickar()
	{
		this.jogo.reproduzirEfeitoSonoro(this.kick);
		vVertical = (int) (-vVertical * 1.5);
	}
	
	public ESTADO getEstado()
	{
		return estado;
	}
	
	public abstract void moverNaHorizontal(int vel);
	
	public abstract void atualizar(); //estava na superclasse
}
