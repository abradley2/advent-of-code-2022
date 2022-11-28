module Page.Index exposing (Data, Model, Msg, page)

import Calendar exposing (CalendarDate)
import DataSource exposing (DataSource)
import Date exposing (Date)
import Head
import Head.Seo as Seo
import Html as H
import Html.Attributes as A
import Page exposing (Page, StaticPayload)
import Pages.PageUrl exposing (PageUrl)
import Pages.Url
import Random exposing (Seed)
import Shared
import Time exposing (Month(..))
import View exposing (View)


fakeBit : String
fakeBit =
    List.foldr
        (\_ ( prevBits, prevSeed ) ->
            Random.step (Random.int 0 9) prevSeed
                |> Tuple.mapFirst (String.fromInt >> (++) prevBits)
        )
        ( "", Random.initialSeed 0 )
        (List.range 0 100)
        |> Tuple.first


type alias Model =
    ()


type alias Msg =
    Never


type alias RouteParams =
    {}


page : Page RouteParams Data
page =
    Page.single
        { head = head
        , data = data
        }
        |> Page.buildNoState { view = view }


data : DataSource Data
data =
    DataSource.succeed ()


head :
    StaticPayload Data RouteParams
    -> List Head.Tag
head _ =
    Seo.summary
        { canonicalUrlOverride = Nothing
        , siteName = "Tony's Advent of Code 2022"
        , image =
            { url = Pages.Url.external "TODO"
            , alt = "elm-pages logo"
            , dimensions = Nothing
            , mimeType = Nothing
            }
        , description = "My Advent of Code 2022 frontend"
        , locale = Nothing
        , title = "Advent of Code 2022"
        }
        |> Seo.website


type alias Data =
    ()


december : List (List CalendarDate)
december =
    Calendar.fromDate Nothing
        (Date.fromCalendarDate 2022 Dec 1)


isAdvent : Date -> Bool
isAdvent date =
    Date.month date == Dec && Date.day date >= 1 && Date.day date <= 25


view :
    Maybe PageUrl
    -> Shared.Model
    -> StaticPayload Data RouteParams
    -> View Msg
view _ _ _ =
    { title = "Advent of Code 2022"
    , body =
        [ H.div
            [ A.class "index-page"
            ]
            [ H.div
                [ A.class "infinite-scroll"
                ]
                [ H.div
                    [ A.class "infinite-scroll__content infinite-scroll__content--1" ]
                    [ H.text fakeBit
                    ]
                , H.div
                    [ A.class "infinite-scroll__content infinite-scroll__content--2" ]
                    [ H.text fakeBit
                    ]
                ]
            , H.div
                [ A.class "title" ]
                [ H.text "2022" ]
            , H.div
                [ A.class "calendar" ]
                (H.div
                    [ A.class "calendar__week" ]
                    (List.map
                        (\symbol ->
                            H.div
                                [ A.class "week__day week__day--no-link" ]
                                [ H.text symbol ]
                        )
                        [ "Su", "Mo", "Tu", "We", "Th", "Fr", "Sa" ]
                    )
                    :: List.map
                        (\week ->
                            H.div
                                [ A.class "calendar__week" ]
                                (List.map
                                    (\day ->
                                        if isAdvent day.date then
                                            H.a
                                                [ A.class "week__day"
                                                , A.href
                                                    ("/solution?day="
                                                        ++ String.fromInt (Date.day day.date)
                                                    )
                                                ]
                                                [ H.span
                                                    []
                                                    [ H.text day.dayDisplay ]
                                                ]

                                        else
                                            H.div
                                                [ A.class "week__day week__day--no-link" ]
                                                []
                                    )
                                    week
                                )
                        )
                        december
                )
            , H.div
                [ A.class "infinite-scroll infinite-scroll--fast"
                ]
                [ H.div
                    [ A.class "infinite-scroll__content infinite-scroll__content--1" ]
                    [ H.text fakeBit
                    ]
                , H.div
                    [ A.class "infinite-scroll__content infinite-scroll__content--2" ]
                    [ H.text (String.reverse fakeBit)
                    ]
                ]
            ]
        ]
    }
