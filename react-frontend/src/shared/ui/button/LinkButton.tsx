import { useNavigate } from "react-router";
import Button, { ButtonStyle } from "./Button";


export default function LinkButton({ text, linkTarget, style }: Props) {
    const navigate = useNavigate();

    function handleClick() {
        if (linkTarget.startsWith("http") || linkTarget.startsWith("www") || linkTarget.startsWith("https")) {
            window.open(linkTarget, "_blank");
            return;
        } else {
            // navigate internally
            navigate(linkTarget);
        }
    }

    return (
        <Button style={style} onClick={handleClick} text={text}/>
    )
}

interface Props {
    text: string;
    linkTarget: string;
    style: ButtonStyle;
}
