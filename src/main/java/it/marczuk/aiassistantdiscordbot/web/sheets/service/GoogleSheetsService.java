package it.marczuk.aiassistantdiscordbot.web.sheets.service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.*;
import it.marczuk.aiassistantdiscordbot.web.sheets.configuration.GoogleSheetsConfig;
import it.marczuk.aiassistantdiscordbot.web.sheets.exception.SheetsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class GoogleSheetsService {

    private static final String APPLICATION_NAME = "AI Assistant Discord Bot";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String SPREADSHEET_ID = "1lAkbOy-giTp1PV2ZerLy3BHGUgBm5Dsim0OksJsiimA";
    private static final String DEFAULT_RANGE = "Class Data!A2:O";

    public List<List<Object>> getGoogleSheetData(String range) throws IOException {
        final Sheets sheetsService = getSheetsService();

        ValueRange response = sheetsService.spreadsheets().values()
                .get(SPREADSHEET_ID, calculateRange(range))
                .execute();
        return response.getValues();
    }

    public List<Object> addGoogleSheetData(List<List<Object>> dataToAdd, String range) throws IOException {
        final Sheets sheetsService = getSheetsService();

        ValueRange appendBody = new ValueRange()
                .setValues(dataToAdd);

        AppendValuesResponse appendResult = sheetsService.spreadsheets().values()
                .append(SPREADSHEET_ID, calculateRange(range), appendBody)
                .setValueInputOption("USER_ENTERED")
                .setInsertDataOption("INSERT_ROWS")
                .setIncludeValuesInResponse(true)
                .execute();

        return new ArrayList<>(appendResult.getUpdates().values());
    }

    public List<Object> deleteGoogleSheetData(int index) throws IOException {
        final Sheets sheetsService = getSheetsService();

        DeleteDimensionRequest deleteRequest = new DeleteDimensionRequest()
                .setRange(
                        new DimensionRange()
                                .setSheetId(0)
                                .setDimension("ROWS")
                                .setStartIndex(index)
                );

        ArrayList<Request> requests = new ArrayList<>();
        requests.add(new Request().setDeleteDimension(deleteRequest));

        BatchUpdateSpreadsheetRequest body = new BatchUpdateSpreadsheetRequest().setRequests(requests);
        BatchUpdateSpreadsheetResponse response = sheetsService.spreadsheets().batchUpdate(SPREADSHEET_ID, body).execute();
        return new ArrayList<>(response.values());
    }

    private Sheets getSheetsService() {
        try {
            final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

            return new Sheets.Builder(httpTransport, JSON_FACTORY, GoogleSheetsConfig.getCredentials(httpTransport))
                    .setApplicationName(APPLICATION_NAME)
                    .build();
        } catch (IOException | GeneralSecurityException e) {
            log.error("SheetsService Error: " + e.getMessage());
            throw new SheetsException(e.getMessage());
        }
    }

    private String calculateRange(String range) {
        return range == null || range.isEmpty() ? DEFAULT_RANGE : range;
    }
}
