package org.blume.wikibase;

/*
 * #%L
 * Wikidata Toolkit Examples
 * %%
 * Copyright (C) 2014 - 2015 Wikidata Toolkit Developers
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.io.IOException;

import org.wikidata.wdtk.datamodel.helpers.ItemDocumentBuilder;
import org.wikidata.wdtk.datamodel.interfaces.ItemDocument;
import org.wikidata.wdtk.datamodel.interfaces.ItemIdValue;
import org.wikidata.wdtk.util.WebResourceFetcherImpl;
import org.wikidata.wdtk.wikibaseapi.ApiConnection;
import org.wikidata.wdtk.wikibaseapi.LoginFailedException;
import org.wikidata.wdtk.wikibaseapi.WikibaseDataEditor;
import org.wikidata.wdtk.wikibaseapi.apierrors.EditConflictErrorException;
import org.wikidata.wdtk.wikibaseapi.apierrors.MediaWikiApiErrorException;
import org.wikidata.wdtk.wikibaseapi.apierrors.NoSuchEntityErrorException;

/**
 * This example shows how to create and modify data through the web API of a
 * Wikibase site.
 * <p>
 * IMPORTANT: Running this program will perform edits on test.wikidata.org.
 * These edits are permanent and public. When running this program as is, the
 * edits will be performed without logging in. This means that your current IP
 * address will be recorded in the edit history of the page. If you prefer to
 * use a login, please comment-in the respective line in the source code and
 * modify it to use your credentials.
 * <p>
 * Note that all modification operations can throw an
 * {@link MediaWikiApiErrorException} (if there was an API error) or
 * {@link IOException} (if there was a network error, etc.). We do not handle
 * this here. In real applications, you may want to handle some subclasses of
 * {@link MediaWikiApiErrorException} in special ways, e.g.,
 * {@link EditConflictErrorException} (you tried to edit an entity that has been
 * modified by someone else since) and {@link NoSuchEntityErrorException} (you
 * tried to modify an entity that meanwhile was deleted).
 *
 * @author Markus Kroetzsch
 *
 */
public class CreateNewItem {

    /**
     * We use this to identify the site test.wikidata.org. This IRI is not
     * essential for API interactions (the API knows of only one site and will
     * use local ids only), but it is important to use a fixed IRI in your code
     * for each site and not to mix IRIs.
     */
    final static String siteIri = "http://w.b-ol.de/entity/";

    public static void main(String[] args) throws LoginFailedException,
            IOException, MediaWikiApiErrorException {

        // Always set your User-Agent to the name of your application:
        WebResourceFetcherImpl
                .setUserAgent("Wikibase Importer");

        ApiConnection connection = ApiConnection.getWikibaseApiConnection();
        WikibaseDataEditor wbde = new WikibaseDataEditor(connection, siteIri);

        System.out.println("*** Creating a new entity ...");

        ItemIdValue noid = ItemIdValue.NULL; // used when creating new items
        ItemDocument itemDocument = ItemDocumentBuilder.forItemId(noid)
                .withLabel("Stone", "en")
                .build();

        ItemDocument newItemDocument = wbde.createItemDocument(itemDocument,
                "Wikibase Importer New Item");

        ItemIdValue newItemId = newItemDocument.getItemId();
        System.out.println("*** Successfully created a new item "
                + newItemId.getId()
                + " (see https://test.wikidata.org/w/index.php?title="
                + newItemId.getId() + "&oldid="
                + newItemDocument.getRevisionId() + " for this version)");

        System.out.println("*** Done.");
    }
}

