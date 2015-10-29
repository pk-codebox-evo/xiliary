package com.codeaffine.eclipse.ui.progress;

import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createShell;
import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;

public class TableViewerAdapterTest {

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private TableViewerAdapter adapter;
  private TableViewer tableViewer;

  @Before
  public void setUp() {
    Shell shell = createShell( displayHelper );
    tableViewer = new TableViewer( shell );
    adapter = new TableViewerAdapter( tableViewer );
  }

  @Test
  public void addElements() {
    Object[] children = { new Object() };

    adapter.addElements( null, children );

    assertThat( tableViewer.getTable().getItemCount() ).isEqualTo( 1 );
  }

  @Test
  public void remove() {
    Object[] children = { new Object() };
    adapter.addElements( null, children );

    adapter.remove( children[ 0 ] );

    assertThat( tableViewer.getTable().getItemCount() ).isEqualTo( 0 );
  }

}