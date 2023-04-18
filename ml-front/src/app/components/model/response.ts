
export interface ResponseFeedFoward{
    resource: ResourceFeedfoward;
    messageStatus: string;
    status: number;
}

export interface ResponseTrain{
    resource: ResourceTrain;
    messageStatus: string;
    status: number;
}

export interface ResponseList{
    resource: ResourceUUIDs;
    messageStatus: string;
    status: number;
}

export interface ResponseNeuralNetwork{
    resource: ResourceNeuralNetwork;
    messageStatus: string;
    status: number;
}

interface ResourceFeedfoward{
    uuid: string;
    outputs: number[];
}

interface ResourceTrain{
    uuid: string;
    outputs: number[];
    bias1: number[];
    bias2: number[];
    hiddenWeights: number[][];
    outputWeights: number[][];
}

interface ResourceUUIDs{
    uuids: string[];
}

interface ResourceNeuralNetwork{
    uuid: string;
    hiddenWeights: number[][];
    outputWeights: number[][];
    bias1: number[];
    bias2: number[];
    outputs: number[];
}