module Page.Solution exposing (Data, Model, Msg, page)

import Browser.Navigation exposing (Key)
import DataSource exposing (DataSource)
import Dict
import Head
import Head.Seo as Seo
import Html as H exposing (Html)
import Html.Attributes as A
import Html.Lazy as Lazy
import Http
import Json.Decode as Decode
import Json.Encode as Encode exposing (Value)
import Page exposing (PageWithState, StaticPayload)
import Pages.PageUrl exposing (PageUrl)
import Pages.Url
import Path exposing (Path)
import Process
import QueryParams
import Random exposing (Generator, Seed)
import Shared
import Task
import View exposing (View)


type SolutionData
    = Loading
    | Success Value
    | Failure Http.Error


type alias Model =
    { loading : Bool
    , solution : SolutionData
    , bits : String
    , seed : Seed
    }


type Msg
    = DoneLoading
    | AnimateBits
    | ReceivedSolution (Result Http.Error Value)


type alias RouteParams =
    {}


fakeBit : Generator Int
fakeBit =
    Random.int 0 9


makeFakeBits : Seed -> ( String, Seed )
makeFakeBits seed =
    List.foldr
        (\_ ( prevBits, prevSeed ) ->
            Random.step fakeBit prevSeed
                |> Tuple.mapFirst (String.fromInt >> (++) prevBits)
        )
        ( "", seed )
        (List.range 0 100)


init :
    Maybe PageUrl
    -> Shared.Model
    -> StaticPayload Data RouteParams
    -> ( Model, Cmd Msg )
init pageUrl shared _ =
    let
        day : String
        day =
            pageUrl
                |> Maybe.andThen .query
                |> Maybe.map QueryParams.toDict
                |> Maybe.andThen (Dict.get "day")
                |> Maybe.andThen List.head
                |> Maybe.withDefault "0"

        ( bits, seed ) =
            Random.initialSeed 0 |> makeFakeBits
    in
    ( { loading = True
      , solution = Loading
      , bits = bits
      , seed = seed
      }
    , Cmd.batch
        [ Http.request
            { method = "GET"
            , url = shared.apiRoot ++ "/api/solution/" ++ day
            , timeout = Just (1000 * 30)
            , expect = Http.expectJson ReceivedSolution Decode.value
            , tracker = Nothing
            , headers = []
            , body = Http.emptyBody
            }
        , Task.perform identity (Task.succeed AnimateBits)
        , Process.sleep 2000
            |> Task.andThen (\_ -> Task.succeed DoneLoading)
            |> Task.perform identity
        ]
    )


subscriptions :
    Maybe PageUrl
    -> RouteParams
    -> Path
    -> Model
    -> Sub Msg
subscriptions _ _ _ _ =
    Sub.none


update :
    PageUrl
    -> Maybe Key
    -> Shared.Model
    -> StaticPayload Data RouteParams
    -> Msg
    -> Model
    -> ( Model, Cmd Msg )
update _ _ _ _ msg model =
    case msg of
        AnimateBits ->
            let
                ( nextBits, nextSeed ) =
                    makeFakeBits model.seed
            in
            ( { model | bits = nextBits, seed = nextSeed }
            , if model.loading then
                Process.sleep 100
                    |> Task.andThen (\_ -> Task.succeed AnimateBits)
                    |> Task.perform identity

              else
                Cmd.none
            )

        DoneLoading ->
            ( { model | loading = False }, Cmd.none )

        ReceivedSolution (Ok result) ->
            ( { model | solution = Success result }, Cmd.none )

        ReceivedSolution (Err err) ->
            ( { model | solution = Failure err }, Cmd.none )


page : PageWithState RouteParams Data Model Msg
page =
    Page.single
        { head = head
        , data = data
        }
        |> Page.buildWithLocalState
            { view = view
            , init = init
            , subscriptions = subscriptions
            , update = update
            }


data : DataSource Data
data =
    DataSource.succeed ()


head :
    StaticPayload Data RouteParams
    -> List Head.Tag
head _ =
    Seo.summary
        { canonicalUrlOverride = Nothing
        , siteName = "elm-pages"
        , image =
            { url = Pages.Url.external "TODO"
            , alt = "elm-pages logo"
            , dimensions = Nothing
            , mimeType = Nothing
            }
        , description = "TODO"
        , locale = Nothing
        , title = "TODO title" -- metadata.title -- TODO
        }
        |> Seo.website


type alias Data =
    ()


view :
    Maybe PageUrl
    -> Shared.Model
    -> Model
    -> StaticPayload Data RouteParams
    -> View Msg
view _ _ model _ =
    { title = "Solution"
    , body =
        [ H.div
            [ A.class "solution-page"
            ]
            [ H.a
                [ A.href "/"
                , A.class "solution-page__back"
                ]
                [ H.text "Return to calendar"
                ]
            , outputContainerView <|
                case ( model.loading, model.solution ) of
                    ( False, Success solution ) ->
                        solutionView solution

                    ( False, Failure error ) ->
                        errorView error

                    ( False, Loading ) ->
                        loadingView model

                    ( True, _ ) ->
                        loadingView model
            ]
        ]
    }


outputContainerView : Html Msg -> Html Msg
outputContainerView =
    List.singleton
        >> H.div
            [ A.class "solution-page__output"
            ]


errorView : Http.Error -> Html Msg
errorView =
    Lazy.lazy errorView_


errorView_ : Http.Error -> Html Msg
errorView_ _ =
    H.div
        []
        [ H.text "Error processing solution :(" ]


loadingView : Model -> Html Msg
loadingView model =
    H.div
        [ A.class "loading-text" ]
        [ H.text "BEEP BOOP..."
        , H.br [] []
        , H.text "CALCULATING..."
        , H.br [] []
        , H.text "BEEP BOOP..."
        , H.br [] []
        , H.p [] [ H.text model.bits ]
        ]


solutionView : Value -> Html Msg
solutionView =
    Lazy.lazy solutionView_


solutionView_ : Value -> Html Msg
solutionView_ value =
    H.pre
        []
        [ H.text (Encode.encode 1 value)
        ]
