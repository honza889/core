/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @author tags. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package org.jboss.as.console.client.plugins;

import static org.jboss.as.console.client.plugins.OperationMode.DOMAIN;
import static org.jboss.as.console.client.plugins.OperationMode.STANDALONE;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Search index meta data for presenters. This annotation works as add-on for {@link
 * org.jboss.as.console.spi.AccessControl} which is the primarily source for the search index. Without specifying
 * {@code SearchIndex} on a presenter, the presenter is indexed using the defaults specified here. Only use
 * {@code SearchIndex} on a presenter if you want to make an exception, like saying that the presenter should be
 * only in the index for standalone or if you want to boost the presenter by setting keywords.
 *
 * @author Harald Pehl
 */
// Actually this annotation should be in org.jboss.as.console.spi, but then the hosted mode will throw
// an ArrayStoreException caused by an AnnotationTypeMismatchExceptionProxy. It seems to be related to
// the fact that the OperationMode and this annotation are processed differently by APT and GWT?
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface SearchIndex {

    /**
     * The scope of the presenter. The console manages different search indexes depending on the operation mode.
     *
     * @return the scope of the presenter
     */
    OperationMode[] scope() default {STANDALONE, DOMAIN};

    /**
     * Set of keywords which will be added to the index for that presenter. Using keywords you can boost the presenter
     * in the search result. Please use sparingly.
     *
     * @return an array with keywords to include in the search index
     */
    String[] keywords() default {};

    /**
     * Using this flag you can exclude a presenter from the search index.
     *
     * @return whether to exclude the presenter
     */
    boolean exclude() default false;
}
