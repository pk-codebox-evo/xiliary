/**
 * Copyright (c) 2014 - 2016 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.eclipse.swt.widget.action;

import static com.codeaffine.eclipse.swt.test.util.SWTEventHelper.trigger;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.util.ButtonClick;

public class ActionSelectorTest {

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private ActionSelector selector;
  private Runnable action;

  @Before
  public void setUp() {
    action = mock( Runnable.class );
    selector = new ActionSelector( action, displayHelper.createImage( 1, 1 ) );
  }

  @Test
  public void create() {
    Label control = ( Label )selector.create( displayHelper.createShell() );

    assertThat( control ).isNotNull();
    assertThat( control.getImage() ).isNotNull();
  }

  @Test
  public void mouseEnter() {
    Control control = selector.create( displayHelper.createShell() );

    trigger( SWT.MouseEnter ).on( control );

    assertThat( control.getBackground().getRGB() )
      .isEqualTo( displayHelper.getSystemColor( SWT.COLOR_LIST_SELECTION ).getRGB() );
  }

  @Test
  public void mouseEnterAndExit() {
    Control control = selector.create( displayHelper.createShell() );
    Color expected = control.getBackground();

    trigger( SWT.MouseEnter ).on( control );
    trigger( SWT.MouseExit ).on( control );

    assertThat( control.getBackground().getRGB() )
      .isEqualTo( expected.getRGB() );
  }

  @Test
  public void mouseClick() {
    Control control = selector.create( displayHelper.createShell() );

    trigger( SWT.MouseDown ).withButton( ButtonClick.LEFT_BUTTON ).on( control );
    trigger( SWT.MouseUp ).on( control );

    verify( action ).run();
  }
}