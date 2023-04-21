
export interface Response <T>{
    resource: T;
    messageStatus: string;
    status: number;
}

export interface ResponseError{
    messageError: string;
    messageStatus: string;
    status: number;
}

export interface ResponseFeedfoward{
    uuid: string;
    outputs: number[];
}

export interface ResponseTrain{
    uuid: string;
    outputs: number[];
    bias1: number[];
    bias2: number[];
    hiddenWeights: number[][];
    outputWeights: number[][];
}

export interface ResponseUUIDs{
    uuids: string[];
}

export interface ResponseNeuralNetwork{
    neuralNetwork: {
        uuid: string;
        hiddenWeights: number[][];
        outputWeights: number[][];
        bias1: number[];
        bias2: number[];
        outputs: number[];
    }
}