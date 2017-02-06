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

package etc;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import componentes.ObjetoAnimado.DIRECAO;

import jogo.SMPropulsion;

public class OuvinteDeTeclado extends KeyAdapter
{
	private SMPropulsion jogo;
	
	public OuvinteDeTeclado(SMPropulsion jogo)
	{
		this.jogo = jogo;
	}
	
	public void keyPressed(KeyEvent e)
	{
		if(jogo.getMario().controlavel)
		{
			if(e.getKeyCode() == KeyEvent.VK_ENTER)
				jogo.pausar();
			else if(e.getKeyCode() == KeyEvent.VK_SHIFT || e.getKeyCode() == KeyEvent.VK_X)
				jogo.getMario().correr();
			else if(e.getKeyCode() == KeyEvent.VK_RIGHT)
				jogo.getMario().andar(DIRECAO.DIREITA);
			else if(e.getKeyCode() == KeyEvent.VK_LEFT)
				jogo.getMario().andar(DIRECAO.ESQUERDA);
			else if(e.getKeyCode() == KeyEvent.VK_DOWN)
				jogo.getMario().abaixar();
			else if(e.getKeyCode() == KeyEvent.VK_UP)
				jogo.getMario().olharParaCima();
			else if(e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_C)
				jogo.getMario().saltar();
			else if(e.getKeyCode() == KeyEvent.VK_Z)
				jogo.getMario().soltarGases();
			else if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
				jogo.encerrarAplicacao();
		}
	}
		
	public void keyReleased(KeyEvent e)
	{
		if(jogo.getMario().controlavel)
		{
			if(e.getKeyCode() == KeyEvent.VK_SHIFT || e.getKeyCode() == KeyEvent.VK_X)
			{
				jogo.getMario().velMax = jogo.getMario().getvMaxAndando();
				jogo.getMario().correndo = false;
			}
			else if(e.getKeyCode() == KeyEvent.VK_RIGHT)
				jogo.getMario().moverParaDireita = false;
			else if(e.getKeyCode() == KeyEvent.VK_LEFT)
				jogo.getMario().moverParaEsquerda = false;
			else if(e.getKeyCode() == KeyEvent.VK_DOWN)
				jogo.getMario().levantar();
			else if(e.getKeyCode() == KeyEvent.VK_UP)
			{
				if(!jogo.getMario().noAr)
					jogo.getMario().cima = false;
			}
			else if(e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_C)
				jogo.getMario().impulsionar = false;
		}
	}
}
