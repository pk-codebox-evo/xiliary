package com.codeaffine.eclipse.swt.widget.scrollbar;

import static com.codeaffine.eclipse.swt.widget.scrollbar.ComponentDistribution.BUTTON_LENGTH;
import static java.lang.Math.max;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public enum Orientation {

  HORIZONTAL {

    @Override
    protected void layout( FlatScrollBar scrollBar ) {
      ComponentDistribution distribution = calculateComponentDistribution( scrollBar );
      Rectangle[] componentBounds = calculateComponentBounds( distribution, scrollBar );
      applyComponentBounds( scrollBar, componentBounds );
    }

    private ComponentDistribution calculateComponentDistribution( FlatScrollBar scrollBar ) {
      return calculateComponentDistribution( scrollBar, getControlBounds( scrollBar ).width );
    }

    private Rectangle[] calculateComponentBounds( ComponentDistribution distribution, FlatScrollBar scrollBar ) {
      int width = getControlBounds( scrollBar ).width;
      int height = getControlBounds( scrollBar ).height - BAR_BREADTH + 1;
      int balance = getRoundingBalance( distribution, scrollBar );
      return new Rectangle[] {
        calcButtons( width, $( 0, CLEARANCE, BUTTON_LENGTH, height ) ),
        $( BUTTON_LENGTH, CLEARANCE, distribution.upFastLength, height ),
        calcDrag( distribution, $( distribution.dragStart, CLEARANCE, distribution.dragLength + balance, height ) ),
        $( distribution.downFastStart, CLEARANCE, distribution.downFastLength - balance, height ),
        calcButtons( width, $( distribution.downStart, CLEARANCE, BUTTON_LENGTH, height ) )
      };
    }

    private Rectangle calcButtons( int length, Rectangle bounds ) {
      Rectangle result = bounds;
      if( length <= ComponentDistribution.BUTTON_LENGTH * 2 ) {
        int downStart = calcDownStartForSmallLength( bounds.x, length );
        result = $( downStart, CLEARANCE, length / 2, bounds.height );
      }
      return result;
    }

    @Override
    protected void setDefaultSize( Control control ) {
      Point size = control.getSize();
      control.setSize( size.x, BAR_BREADTH );
    }

    @Override
    protected Point computeSize( Composite composite, int wHint, int hHint, boolean changed ) {
      int x = wHint == SWT.DEFAULT ? composite.getParent().getClientArea().width : wHint;
      return new Point( x, BAR_BREADTH );
    }

    @Override
    protected void expand( Control control ) {
      Rectangle bounds = control.getBounds();
      int expand = expand( bounds.height );
      control.setBounds( bounds.x, bounds.y - expand, bounds.width, bounds.height + expand );
      control.moveAbove( null );
    }
  },

  VERTICAL {

    @Override
    protected void layout( FlatScrollBar scrollBar ) {
      ComponentDistribution calculation = calculateComponentDistribution( scrollBar );
      applyComponentBounds( scrollBar, calculateComponentBounds( calculation, scrollBar ) );
    }

    private ComponentDistribution calculateComponentDistribution( FlatScrollBar scrollBar ) {
      return calculateComponentDistribution( scrollBar, getControlBounds( scrollBar ).height );
    }

    private Rectangle[] calculateComponentBounds( ComponentDistribution distribution, FlatScrollBar scrollBar ) {
      int width = getControlBounds( scrollBar ).width - BAR_BREADTH + 1;
      int height = getControlBounds( scrollBar ).height;
      int balance = getRoundingBalance( distribution, scrollBar );
      return new Rectangle[] {
        calculateButtons( height, $( CLEARANCE, 0, width, BUTTON_LENGTH ) ),
        $( CLEARANCE, BUTTON_LENGTH, width, distribution.upFastLength ),
        calcDrag( distribution, $( CLEARANCE, distribution.dragStart, width, distribution.dragLength + balance ) ),
        $( CLEARANCE, distribution.downFastStart, width, distribution.downFastLength - balance ),
        calculateButtons( height, $( CLEARANCE, distribution.downStart, width, BUTTON_LENGTH ) )
      };
    }

    private Rectangle calculateButtons( int length, Rectangle bounds ) {
      Rectangle result = bounds;
      if( length <= ComponentDistribution.BUTTON_LENGTH * 2 ) {
        int downStart = calcDownStartForSmallLength( bounds.y, length );
        result = $( CLEARANCE, downStart, bounds.width, length / 2 );
      }
      return result;
    }

    @Override
    protected void setDefaultSize( Control control ) {
      Point size = control.getSize();
      control.setSize( BAR_BREADTH, size.y );
    }

    @Override
    protected Point computeSize( Composite composite, int wHint, int hHint, boolean changed ) {
      int y = hHint == SWT.DEFAULT ? composite.getParent().getClientArea().height : hHint;
      return new Point( BAR_BREADTH, y );
    }

    @Override
    protected void expand( Control control ) {
      Rectangle bounds = control.getBounds();
      int expand = expand( bounds.width );
      control.setBounds( bounds.x - expand, bounds.y, bounds.width + expand, bounds.height );
      control.moveAbove( null );
    }
  };

  static final Rectangle EMPTY_RECTANGLE = $( 0, 0, 0, 0 );
  static final int BAR_BREADTH = 6;
  static final int CLEARANCE = BAR_BREADTH - 2;
  static final int MAX_EXPAND = CLEARANCE;

  protected abstract void layout( FlatScrollBar scrollBar );
  protected abstract void setDefaultSize( Control control );
  protected abstract Point computeSize( Composite comp, int wHint, int hHint, boolean changed );
  protected abstract void expand( Control control );

  private static ComponentDistribution calculateComponentDistribution( FlatScrollBar scrollBar, int length ) {
    int range = scrollBar.getMaximum() - scrollBar.getMinimum();
    int position = scrollBar.getSelection() - scrollBar.getMinimum();
    int thumb = scrollBar.getThumb();
    return new ComponentDistribution( length, range, position, thumb );
  }

  private static Rectangle getControlBounds( FlatScrollBar scrollBar ) {
    return scrollBar.control.getClientArea();
  }

  private static void applyComponentBounds( FlatScrollBar scrollBar, Rectangle[] bounds ) {
    scrollBar.up.getControl().setBounds( bounds[ 0 ] );
    scrollBar.upFast.getControl().setBounds( bounds[ 1 ] );
    scrollBar.drag.getControl().setBounds( bounds[ 2 ] );
    scrollBar.downFast.getControl().setBounds( bounds[ 3 ] );
    scrollBar.down.getControl().setBounds( bounds[ 4 ] );
  }

  // TODO [fappel]: There is a 1 pixel rounding problem at the seam of drag/downFast with down.
  //                Seems to work but I would prefer a better solution if possible
  private static int getRoundingBalance( ComponentDistribution calculation, FlatScrollBar scrollBar ) {
    int result = 0;
    int maximumSelection = scrollBar.getMaximum() - scrollBar.getThumb();
    if( scrollBar.getSelection() == maximumSelection && 0 != calculation.downFastLength ) {
      result = 1;
    }
    return result;
  }

  private static int expand( int toExpand ) {
    return max( 0, BAR_BREADTH + MAX_EXPAND - max( BAR_BREADTH, toExpand ) );
  }

  private static Rectangle calcDrag( ComponentDistribution distribution, Rectangle bounds ) {
    Rectangle result = bounds;
    if( isUndercutOfDragVisibility( distribution ) ) {
      result = EMPTY_RECTANGLE;
    }
    return result;
  }

  private static boolean isUndercutOfDragVisibility( ComponentDistribution distribution ) {
    return distribution.dragLength + BUTTON_LENGTH >= distribution.downStart;
  }

  private static int calcDownStartForSmallLength( int position, int length ) {
    int result = position;
    if( isDownStartPosition( position ) ) {
      result = length / 2;
    }
    return result;
  }
  private static boolean isDownStartPosition( int position ) {
    return position > 0 || position < 0;
  }

  private static Rectangle $( int x, int y, int width, int height ) {
    return new Rectangle( x, y , width , height );
  }
}