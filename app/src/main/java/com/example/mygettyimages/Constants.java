package com.example.mygettyimages;

public class Constants {

    public static final class Debug {

        public static final String TAG = "mygettyimages";

        public static final String DEBUG_TAG = "mygettyimagesDebug";

        public static final String ERROR_TAG = "mygettyimagesError";

        public static final String DEBUG_HTTP_TAG = "mygettyimagesHTTPDebug";

        public static final boolean IS_DEBUG_HTTP = true;
    }

    public static final class UI {

        public static final class ActionBarType {

            public static final int EMPTY_ACTION_BAR = 1;

            public static final int SEARCH_ACTION_BAR = 2;
        }

    }

    public static final class FieldValue {

        public static final int UNDEFINED_FIELD_VALUE = -2;
    }

    public static final class MainScreenFragmentsTags {

        public static final String NO_TAG = "NO_TAG";
    }

    public static final class Data {

        public static final int MIN_SEARCH_QUERY_LENGTH = 2;

        public static final int MAX_SEARCH_QUERY_LENGTH = 50;

        public static final int SEARCH_COUNT = 30;
    }

    public static final class Server {

        public static final class Urls {

            public static final String BASE_URL = "https://api.gettyimages.com/v3/";

            public static final String IMAGE_SEARCH_URL = BASE_URL + "search/images";

            public static final String PHRASE_QUERY = "phrase";

            public static final String FIELDS_QUERY = "fields";

            public static final String PAGE_SIZE_QUERY = "page_size";

            public static final String SORT_ORDER_QUERY = "sort_order";

            public static final String FIELDS_VALUE = "id,max_dimensions,preview,thumb,title";

            public static final String THUMB_VALUE = "thumb";

            public static final String BEST_MATH_VALUE = "best_match";

        }

        public static final class ServerResponses {

            public static final String INVALID_ACCESS_TOKEN = "Invalid Access token";

            public static final String SERVER_ERROR_STATUS = "Error";
        }

        public static final class ServerSettings {

            public static final int CONNECTION_CONNECT_TIMEOUT = 30000;//15000);

            public static final int CONNECTION_READ_TIMEOUT = 20000;//10000);
        }

        public static final class Auth {

            public static final String GETTY_IMAGES_API_KEY_NAME = "Api-Key";

            public static final String GETTY_IMAGES_API_KEY = "mkx7nswmpgyvpxxr7r2jbt7k";

        }

    }

}