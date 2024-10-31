//=====================================================================
// Copyright (C) 2019 Instrumental Software Technologies, Inc.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions
// are met:
// 1. Redistributions of source code, or portions of this source code,
//    must retain the above copyright notice, this list of conditions
//    and the following disclaimer.
// 2. Redistributions in binary form must reproduce the above copyright
//    notice, this list of conditions and the following disclaimer in
//    the documentation and/or other materials provided with the
//    distribution.
// 3. All advertising materials mentioning features or use of this
//    software must display the following acknowledgment:
//    "This product includes software developed by Instrumental
//    Software Technologies, Inc. (http://www.isti.com)"
// 4. If the software is provided with, or as part of a commercial
//    product, or is used in other commercial software products the
//    customer must be informed that "This product includes software
//    developed by Instrumental Software Technologies, Inc.
//    (http://www.isti.com)"
// 5. The names "Instrumental Software Technologies, Inc." and "ISTI"
//    must not be used to endorse or promote products derived from
//    this software without prior written permission. For written
//    permission, please contact "info@isti.com".
// 6. Products derived from this software may not be called "ISTI"
//    nor may "ISTI" appear in their names without prior written
//    permission of Instrumental Software Technologies, Inc.
// 7. Redistributions of any form whatsoever must retain the following
//    acknowledgment:
//    "This product includes software developed by Instrumental
//    Software Technologies, Inc. (http://www.isti.com/)."
// THIS SOFTWARE IS PROVIDED BY INSTRUMENTAL SOFTWARE
// TECHNOLOGIES, INC. "AS IS" AND ANY EXPRESSED OR IMPLIED
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
// OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
// DISCLAIMED.  IN NO EVENT SHALL INSTRUMENTAL SOFTWARE TECHNOLOGIES,
// INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
// INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
// (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
// SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
// HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
// STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
// OF THE POSSIBILITY OF SUCH DAMAGE.
//=====================================================================
//  A current version of the software can be found at
//                http://www.isti.com
//  Bug reports and comments should be directed to
//  Instrumental Software Technologies, Inc. at info@isti.com
//=====================================================================
//

package com.isti.util.mac;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.apple.eawt.AppEvent.QuitEvent;
import com.apple.eawt.QuitResponse;

/**
 * Mac utility functions.
 * 
 * @author ISTI
 * 
 */
public class MacUtilFns {
	/** The Apple look and feel use screen menu bar property key. */
	private static final String USESCREENMENUBAR_KEY = "apple.laf.useScreenMenuBar";

	/**
	 * Get the operating system.
	 * 
	 * @return the operating system or an empty string if none.
	 */
	public static String getOperatingSystem() {
		return System.getProperty("os.name", "");
	}

	/**
	 * Determines if the operating system is Mac.
	 * 
	 * @return true if the operating system is Mac, false otherwise.
	 */
	public static boolean isMac() {
		return getOperatingSystem().toLowerCase().indexOf("mac") != -1;
	}

	/**
	 * Determines if the screen menu bar should be used on the Mac.
	 * 
	 * @return true if the screen menu bar should be used, false otherwise.
	 */
	public static boolean isUseScreenMenuBar() {
		return Boolean.getBoolean(USESCREENMENUBAR_KEY);
	}

	/**
	 * Run is Mac test.
	 * 
	 * @param args
	 *            the arguments.
	 */
	public static void main(String[] args) {
		System.out.println("os=" + getOperatingSystem() + ", isMac=" + isMac());
	}

	/**
	 * Set the about handler for the Mac about menu option. This should only be
	 * called if Mac.
	 * 
	 * @param actionListener
	 *            the action listener.
	 * @param command
	 *            the action command or null if none.
	 * @see #isMac()
	 */
	public static void setAboutHandler(final ActionListener actionListener,
			final String command) {
		setHandler(actionListener, command, null);
	}

	/**
	 * Set the handler for the Mac about and quit menu options. This should only
	 * be called if Mac.
	 * 
	 * @param actionListener
	 *            the action listener.
	 * @param aboutCommand
	 *            the about action command or null if none.
	 * @param quitCommand
	 *            the quit action command or null if none.
	 * @see #isMac()
	 */
	public static void setHandler(final ActionListener actionListener,
			final String aboutCommand, final String quitCommand) {
		// exit if not using the screen menu bar
		if (!Boolean.getBoolean(USESCREENMENUBAR_KEY)) {
			return;
		}
		try {
			com.apple.eawt.Application application = com.apple.eawt.Application
					.getApplication();
			if (aboutCommand != null) {
				application.setAboutHandler(new com.apple.eawt.AboutHandler() {
					@Override
					public void handleAbout(
							com.apple.eawt.AppEvent.AboutEvent e) {
						actionListener.actionPerformed(new ActionEvent(
								e.getSource(), ActionEvent.ACTION_PERFORMED,
								aboutCommand));
					}
				});
			}
			if (quitCommand != null) {
				application.setQuitHandler(new com.apple.eawt.QuitHandler() {
					@Override
					public void handleQuitRequestWith(QuitEvent e,
							QuitResponse qr) {
						actionListener.actionPerformed(new ActionEvent(
								e.getSource(), ActionEvent.ACTION_PERFORMED,
								quitCommand));
					}
				});
			}
		} catch (Throwable t) {
		}
	}

	/**
	 * Set to use the screen menu bar on the Mac if it is not already set. This
	 * should only be called if Mac.
	 * 
	 * @see #isMac()
	 */
	public static void setUseScreenMenuBar() {
		// exit if property is already set
		if (isUseScreenMenuBar()) {
			return;
		}
		System.setProperty(USESCREENMENUBAR_KEY, "true");
	}
}
