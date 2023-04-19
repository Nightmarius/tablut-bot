import styled from "styled-components";
/*use svg as wrapped component*/
export const Crown = () => {
    return (
        <div>
            <svg xmlns="http://www.w3.org/2000/svg" className="icon icon-tabler icon-tabler-crown" width="55"
                 height="55" viewBox="0 0 24 24" stroke-width="1.5" stroke="#2c3e50" fill="white" stroke-linecap="round"
                 stroke-linejoin="round">
                <path stroke="none" d="M0 0h24v24H0z" fill="none"/>
                <path d="M12 6l4 6l5 -4l-2 10h-14l-2 -10l5 4z"/>
            </svg>
        </div>
    );
}
// #ff820a
export const Figure = styled.div<{ color: string }>`
    border-radius: 50%;
    background-color: ${(props) => props.color};
    border: 1px solid black;
    width: 85%;
    height: 85%;
    `;