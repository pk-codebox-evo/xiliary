package com.codeaffine.eclipse.core.runtime;

import static com.codeaffine.eclipse.core.runtime.ArgumentVerification.verifyNotNull;

import com.codeaffine.eclipse.core.runtime.RegistryAdapter.ExecutableExtensionConfiguration;
import com.codeaffine.eclipse.core.runtime.internal.Operator.CreateExecutableExtensionsOperator;

public class CreateMultiProcessor<T> extends ReadMultiProcessor<T> implements ExecutableExtensionConfiguration<T> {

  CreateMultiProcessor( CreateExecutableExtensionsOperator<T> operator ) {
    super( operator );
  }

  @Override
  public CreateMultiProcessor<T> withConfiguration(
    ExecutableExtensionConfigurator<T> configurator )
  {
    verifyNotNull( configurator, "configurator" );

    getOperator().setConfigurator( configurator );
    return this;
  }

  @Override
  public CreateMultiProcessor<T> withExceptionHandler(
    ExtensionExceptionHandler exceptionHandler )
  {
    verifyNotNull( exceptionHandler, "exceptionHandler" );

    getOperator().setExceptionHandler( exceptionHandler );
    return this;
  }

  @Override
  public CreateMultiProcessor<T> withTypeAttribute( String typeAttribute ) {
    verifyNotNull( typeAttribute, "typeAttribute" );

    getOperator().setTypeAttribute( typeAttribute );
    return this;
  }

  private CreateExecutableExtensionsOperator<T> getOperator() {
    return ( CreateExecutableExtensionsOperator<T> )operator;
  }
}