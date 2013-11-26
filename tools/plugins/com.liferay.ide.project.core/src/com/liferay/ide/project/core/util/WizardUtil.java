/*******************************************************************************
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 *******************************************************************************/

package com.liferay.ide.project.core.util;

import com.liferay.ide.core.util.CoreUtil;
import com.liferay.ide.project.core.LiferayProjectCore;
import com.liferay.ide.templates.core.ITemplateContext;
import com.liferay.ide.templates.core.ITemplateOperation;
import com.liferay.ide.templates.core.TemplatesCore;

import java.io.ByteArrayInputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.wst.xml.core.internal.provisional.format.FormatProcessorXML;

/**
 * @author Gregory Amerson
 */
@SuppressWarnings( "restriction" )
public class WizardUtil
{

    public static void createDefaultServiceBuilderFile(
        IFile serviceBuilderFile, String descriptorVersion, boolean useSampleTemplate, String packagePath,
        String namespace, Object author, IProgressMonitor monitor ) throws CoreException
    {
        ITemplateOperation templateOp = null;

        if( useSampleTemplate )
        {
            templateOp = TemplatesCore.getTemplateOperation( "com.liferay.ide.service.core.defaultServiceXmlFile" );
        }
        else
        {
            templateOp = TemplatesCore.getTemplateOperation( "com.liferay.ide.service.core.emptyServiceXmlFile" );
        }

        final ITemplateContext context = templateOp.getContext();

        context.put( "version", descriptorVersion );
        context.put( "version_", descriptorVersion.replace( '.', '_' ) );
        context.put( "package_path", packagePath );
        context.put( "namespace", namespace );
        context.put( "author", author );

        try
        {
            final StringBuffer sb = new StringBuffer();
            templateOp.setOutputBuffer( sb );
            templateOp.execute( monitor );

            CoreUtil.prepareFolder( (IFolder) serviceBuilderFile.getParent() );

            serviceBuilderFile.create(
                new ByteArrayInputStream( sb.toString().getBytes( "UTF-8" ) ), IResource.FORCE, null ); //$NON-NLS-1$

            FormatProcessorXML processor = new FormatProcessorXML();

            processor.formatFile( serviceBuilderFile );
        }
        catch( Exception e )
        {
            LiferayProjectCore.logError( e );
        }

    }

}