package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.widget.scrollable.FlatScrollBarTree.BAR_BREADTH;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Scrollable;


class LayoutContext<T extends Scrollable> {

  static final int OVERLAY_OFFSET = 40;
  static final int WIDTH_BUFFER = 2;

  private final boolean horizontalBarVisible;
  private final boolean verticalBarVisible;
  private final Rectangle visibleArea;
  private final int verticalBarOffset;
  private final Point preferredSize;
  private final Composite adapter;
  private final Point location;
  private final int itemHeight;
  private final T scrollable;
  private final int offset;

  LayoutContext( Composite adapter, T scrollable ) {
    this( adapter, scrollable, 1 );
  }

  private LayoutContext( Composite adapter, T scrollable, int itemHeight ) {
    this.scrollable = scrollable;
    this.adapter = adapter;
    this.itemHeight = itemHeight;
    Point computed = scrollable.computeSize( SWT.DEFAULT, SWT.DEFAULT, true );
    preferredSize = new Point( computed.x + WIDTH_BUFFER, computed.y );
    visibleArea = adapter.getClientArea();
    location = new Point( visibleArea.x, visibleArea.y );
    horizontalBarVisible = preferredSize.x > visibleArea.width;
    verticalBarOffset = computeVerticalBarOffset( scrollable );
    verticalBarVisible
      = computeVerticalBarVisible( horizontalBarVisible, preferredSize.y, visibleArea.height, itemHeight );
    offset = new OffsetComputer( scrollable ).compute();
  }

  LayoutContext<T> newContext( int itemHeight ) {
    return new LayoutContext<T>( adapter, scrollable, itemHeight );
  }

  LayoutContext<T> newContext() {
    return new LayoutContext<T>( adapter, scrollable, itemHeight );
  }

  Composite getAdapter() {
    return adapter;
  }

  T getScrollable() {
    return scrollable;
  }

  int getOffset() {
    return offset;
  }

  Point getLocation() {
    return location;
  }

  Point getPreferredSize() {
    return preferredSize;
  }

  boolean isVerticalBarVisible() {
    return verticalBarVisible;
  }

  boolean isHorizontalBarVisible() {
    return horizontalBarVisible;
  }

  Rectangle getVisibleArea() {
    return visibleArea;
  }

  int getVerticalBarOffset() {
    return verticalBarOffset;
  }

  private static boolean computeVerticalBarVisible(
    boolean horizontalBarVisible, int preferredHeight, int visibleAreaHeight, int itemHeight  )
  {
    boolean result;
    if( !horizontalBarVisible ) {
      result = computeVisibleItemsHeight( preferredHeight, itemHeight ) >= visibleAreaHeight;
    } else {
      result = computeVisibleItemsHeight( preferredHeight, itemHeight ) + BAR_BREADTH - 1 >= visibleAreaHeight;
    }
    return result;
  }

  private static int computeVisibleItemsHeight( int preferredHeight, int itemHeight  ) {
    return ( preferredHeight / itemHeight ) * itemHeight;
  }

  private static int computeVerticalBarOffset( Scrollable scrollable ) {
    int result = scrollable.getVerticalBar().getSize().x;
    if( result == 0 ) {
      result = OVERLAY_OFFSET;
    }
    return result;
  }
}