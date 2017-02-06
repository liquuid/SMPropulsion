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

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.Timer;

import jogo.SMPropulsion;

public class Gasometro implements ActionListener {
	private SMPropulsion jogo;
	private ImageIcon gas, unidade, grade;
	
	public Gasometro(SMPropulsion jogo) {
		this.jogo = jogo;
		this.gas = new ImageIcon("img/objetos/gas3.png");
		this.unidade = new ImageIcon("img/objetos/gasometroUnidade.png");
		this.grade = new ImageIcon("img/objetos/gasometroGrade.png");
		
		new Timer(2500, this).start();
	}
	
	public void desenhar(Graphics g) {
		this.gas.paintIcon(this.jogo, g, 126, 70);
		this.grade.paintIcon(this.jogo, g, 150, 78);
		
		for(int i = 0; i <= this.jogo.getMario().gasAtual(); ++i)
			this.unidade.paintIcon(this.jogo, g, 151 + i * 6, 79);
	}

	public void actionPerformed(ActionEvent e) {
		if(this.jogo.getMario().gasAtual() < 4)
			this.jogo.getMario().gasAtual(this.jogo.getMario().gasAtual() + 1);		
	}
}
