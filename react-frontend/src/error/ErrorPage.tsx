import styled from "styled-components";
import { isRouteErrorResponse, useRouteError } from "react-router";

const ErrorTitle = styled.h1`
    font-size: 1.5em;
    text-align: center;
    color: black;
`;

const ErrorParagraph = styled.p`
    text-align: center;
`;

export default function ErrorPage() {
    const error = useRouteError();
    console.error(error);

    return (
        <div id="error-page">
            <ErrorTitle>Oops!</ErrorTitle>
            <ErrorParagraph>Sorry, an unexpected error has occurred.</ErrorParagraph>
            <ErrorParagraph>
                {isRouteErrorResponse(error) ? <i>{error.statusText}</i> : <i>Unknown error occurred</i>}
            </ErrorParagraph>
        </div>
    );
}
