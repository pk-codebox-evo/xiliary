package com.codeaffine.eclipse.core.runtime.internal;

import org.eclipse.core.runtime.IExtensionRegistry;

import com.codeaffine.eclipse.core.runtime.Extension;
import com.codeaffine.eclipse.core.runtime.internal.Operator.CreateExecutableExtensionOperator;
import com.codeaffine.eclipse.core.runtime.internal.Operator.CreateExecutableExtensionsOperator;
import com.codeaffine.eclipse.core.runtime.internal.Operator.ReadExtensionOperator;
import com.codeaffine.eclipse.core.runtime.internal.Operator.ReadExtensionsOperator;

public class OperatorFactory {

  private final IExtensionRegistry registry;

  public OperatorFactory( IExtensionRegistry registry ) {
    this.registry = registry;
  }

  public ReadExtensionOperator<Extension> newReadExtensionOperator( String extensionPointId ) {
    return new ReadSingleOperator( registry, extensionPointId );
  }

  public ReadExtensionsOperator<Extension> newReadExtensionsOperator( String extensionPointId ) {
    return new ReadMultiOperator( registry, extensionPointId );
  }

  public <T> CreateExecutableExtensionOperator<T> newCreateExecutableExtensionOperator(
    String extensionPointId, Class<T> extensionType )
  {
    return new CreateSingleOperator<T>( registry, extensionPointId, extensionType );
  }

  public <T> CreateExecutableExtensionsOperator<T> newCreateExecutableExtensionsOperator(
    String extensionPointId, Class<T> extensionType )
  {
    return new CreateMultiOperator<T>( registry, extensionPointId, extensionType );
  }
}
