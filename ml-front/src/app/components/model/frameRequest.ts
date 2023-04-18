
export interface FrameRequest {
    frame: Frame;
}

export interface FramesRequest {
    frames: Frame[];
    targets: Target[];
}
interface Frame{
    id: number;
    pixels: Pixel[];
}

interface Pixel{
    value: number;
}

interface Target{
    arrayTarget: number[];
}