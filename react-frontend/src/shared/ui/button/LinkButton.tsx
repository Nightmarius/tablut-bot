import { useNavigate } from "react-router";
import Button, { ButtonStyle } from "./Button";
import { ReactNode } from "react";


export default function LinkButton({ children, linkTarget, style }: Props) {
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
        <Button style={style} onClick={handleClick}>{children}</Button>
    )
}

interface Props {
    children: ReactNode;
    linkTarget: string;
    style: ButtonStyle;
}
