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
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.Timer;
import jogo.SMPropulsion;

public class CuboDeSalto extends Cubo implements Runnable
{
	private static final long serialVersionUID = 1L;
	private File som;
	private int alcance;
	
	public CuboDeSalto(SMPropulsion jogo, Point p1, int alcance)
	{
		super(jogo, p1, new Point(p1.x + 16, p1.y - 16), "cuboDeSalto", 3);
		this.som = new File("sound/wav/springjump.wav");
		this.alcance = alcance;
		timer = new Timer(500, this);
		timer.start();
	}
	
	public void testarColisoesCom(ObjetoAnimado sprite)
	{
		if(colisaoAEsquerdaCom(sprite))
		{
			sprite.vDireita = 0;
			sprite.saltando = false;
		}
		else if(colisaoADireitaCom(sprite))
		{
			sprite.vEsquerda = 0;
			sprite.saltando = false;
		}
		else if(colidiuEmBaixoCom(sprite))
		{
			sprite.vVertical = -sprite.vVertical;
		}
		else if(pisadoPor(sprite))
		{
			personagemATratar = sprite;
			(new Thread(this)).start();
		}
	}

	public void run()
	{
		this.jogo.reproduzirEfeitoSonoro(this.som);
		personagemATratar.vVertical = this.alcance;
		
		this.y += 10;
		try { Thread.sleep(60); }
		catch (Exception e) { e.printStackTrace(); }
		this.y -= 10;
		
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(timer.isRunning())
			imagemAtual = (imagemAtual + 1) % 3;
	}
	
	public void resetar() {

	}
}
